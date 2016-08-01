#!/usr/bin/env node

var fs = require('fs');
var configuration = JSON.parse(fs.readFileSync('.react-vaadin.config', 'utf8'));
var reactDocs = require('react-docgen');
var baseClassTemplate = fs.readFileSync(__dirname + "/templates/BaseClassTemplate.java", "utf8")
var defaultImplClassTemplate = fs.readFileSync(__dirname + "/templates/DefaultImplementationClassTemplate.java", "utf8")
var connectorTemplate = fs.readFileSync(__dirname + "/templates/Connector.js", "utf8")
var Mustache = require("mustache");
require("./reactReflectiveProptypes")
function listFiles(path){
  var stat = fs.statSync(path)
  if(stat.isDirectory()){
    var files = []
    var contents = fs.readdirSync(path)
    for(var i in contents){
      var fullPath = path + "/" + contents[i]
      files = files.concat(listFiles(fullPath))
    }
    return files
  }else{
    if(path.endsWith(".js")){
      return [path]
    }else{
      return []
    }
  }
}

var files = []
for(var i in configuration.components){
  files = files.concat(listFiles(configuration.components[i]))
}

for(var file in files){
  var filePath = "./" + files[file]
  processFile(filePath)
}

function readComponentName(file){
  var splitted = file.split("/");
  var componentWithExtension = splitted[splitted.length - 1]
  return componentWithExtension.split(".")[0]
}
function processFile(file){

  var src = fs.readFileSync(file)
  var componentModule = require(process.cwd()  + "/" + file)
  var componentName = readComponentName(file)
  //Read component from its module. The component will be the module itself if exported as default
  var component = componentModule[componentName] || componentModule

  var processedProps = []
  var requiredProps = []
  var requiredArgList = ""
  var requiredArgListCall = ""

  for(var propName in component.propTypes){
    var prop = component.propTypes[propName]
    var upperName = propName[0].toUpperCase() + propName.substring(1)
    var processedProp = { name : propName, upperName : upperName}
    addJavaType(processedProp, prop.type)
    processedProps.push(processedProp)

    if(prop.required){
      //Create arguments string for the constructor, we are doing it here because it would be uglier in mustache
      if(requiredArgList != ""){
        requiredArgList += ", ";
        requiredArgListCall += ", ";
      }
      requiredArgList += `${processedProp.type} ${prop.name}`
      requiredArgListCall += `${prop.name}`
      requiredProps.push(processedProp)
    }
  }

  var view = {
      name : componentName,
      requiredProps : requiredProps,
      props : processedProps,
      requiredArgList : requiredArgList,
      requiredArgListCall : requiredArgListCall,
      package : configuration.package,
      file : process.cwd() + "/" + file
  };
  var renderedBase = Mustache.render(baseClassTemplate, view);
  var renderedImpl = Mustache.render(defaultImplClassTemplate, view);
  var renderedConnector = Mustache.render(connectorTemplate, view);

  //We are assuming maven (at least for now), we will be writting to target/generated-sources/vaadin-react
  var javaDir = "target/generated-sources/vaadin-react/" + getPackageAsPath(configuration.package) + "/";
  var resourcesDir = "target/classes/" + getPackageAsPath(configuration.package) + "/";
  mkdirs(javaDir);
  mkdirs(resourcesDir);
  fs.writeFileSync(javaDir +  componentName + "_Base.java", renderedBase, 'utf8');
  fs.writeFileSync(javaDir +  componentName + "Impl.java", renderedImpl, 'utf8');

  var connectorPath = resourcesDir +  componentName + "SimpleConnector.js";
  fs.writeFileSync(connectorPath, renderedConnector, 'utf8');

  //export browserified connector
  var browserify = require('browserify');
  var b = browserify();
  b.add(connectorPath);
  b.bundle().pipe(fs.createWriteStream(resourcesDir +  componentName + "Connector.js"));

  //We save the original simple connector and the browserified connector so the user can decide which to use in case he wants to optimize his "widgetset"


  //Copy the lib to the target folder - we are avoiding a maven distribution for now
  var libPackageFolder = "target/generated-sources/vaadin-react/com/nunopinheiro/vaadin_react";
  mkdirs(libPackageFolder);
  fs.createReadStream(__dirname + "/lib/ReactComponent.java", "utf8").pipe(fs.createWriteStream(libPackageFolder + "/ReactComponent.java"));
}

function getPackageAsPath(package){
  //I wanted to spend more time writting a comment about this "replace all" than making a decent function
  while(package.indexOf(".") > 0){
    package = package.replace(".", "/");
  }
  return package;
}

function addJavaType(properties, type){
  // The existing React types can be seen in the official documentation:
  // https://facebook.github.io/react/docs/reusable-components.html
  switch(type) {
    case "string":
      properties.type = "String";
      break;
    case "number":
      properties.type = "double";
      break;
    case "bool":
      properties.type = "boolean";
      break;
    case "func":
      //JavascriptFunction is a special vaadin type which enable handling client-side events on the server
      properties.type = "JavaScriptFunction";
      properties.isFunction = true;
      break;
    case "object":
      properties.type = "elemental.json.JsonObject";
      break;
    case "array":
      properties.type = "elemental.json.JsonArray";
      break;
    case "symbol":
      //The type used on the server-side will be String, but it will be transformed into a Symbol on the client-side
      properties.type = "String";
      properties.isSymbol = true;
      break;
    case "element":
      properties.type = "com.nunopinheiro.vaadin_react.ReactComponent";
      properties.isElement = true;
      break;
    //These validation types are still unsupported
    case "node":
    case "instanceOf":
    case "enum":
    case "union":
    case "arrayOf":
    case "custom":
    case "shape":
    default:
      throw "Unsupported type: " + type
  }
}

function mkdirs(path){
  //Had to do my own mkdirs... WOW
  // I am assuming the path is not absolute (does not start with '/')
  var splitted = path.split("/");
  var path = "";
  for(var part in splitted){
    path += splitted[part] + "/"
    if(!fs.existsSync(path)){
        fs.mkdirSync(path);
    }
  }
}
