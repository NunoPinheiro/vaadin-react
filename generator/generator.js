var fs = require('fs');
var configuration = JSON.parse(fs.readFileSync('.react-vaadin.config', 'utf8'));
var reactDocs = require('react-docgen');
var baseClassTemplate = fs.readFileSync(__dirname + "/templates/BaseClassTemplate.java", "utf8")
var defaultImplClassTemplate = fs.readFileSync(__dirname + "/templates/DefaultImplementationClassTemplate.java", "utf8")
var connectorTemplate = fs.readFileSync(__dirname + "/templates/Connector.js", "utf8")
var Mustache = require("mustache");

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
    return [path]
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

function processFile(file){
  var componentName = getComponentName(file);
  var src = fs.readFileSync(file)
  var componentInfo = reactDocs.parse(src);
  var processedProps = []
  var requiredProps = []
  var requiredArgList = ""
  var requiredArgListCall = ""

  for(var prop in componentInfo.props){
    var type = toJavaType(componentInfo.props[prop].type.name)
    var isFunction = type == "JavascriptFunction"
    var upperName = prop[0].toUpperCase() + prop.substring(1)
    var processedProp = { name : prop, upperName : upperName, type : type, isFunction : isFunction }
    processedProps.push(processedProp)
    if(componentInfo.props[prop].required){
      //Create arguments string for the constructor, we are doing it here because it would be uglier in mustache
      if(requiredArgList != ""){
        requiredArgList += ", ";
        requiredArgListCall += ", ";
      }
      requiredArgList += `${type} ${prop}`
      requiredArgListCall += `${prop}`
      requiredProps.push(processedProp)
    }
  }

  var view = {
      name : componentName,
      props : processedProps,
      requiredProps : requiredProps,
      requiredArgList : requiredArgList,
      requiredArgListCall : requiredArgListCall,
      package : configuration.package
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
  fs.writeFileSync(resourcesDir +  componentName + "Connector.js", renderedConnector, 'utf8');
  //console.log(rendered)
  //console.log(renderedImpl)
}

function getPackageAsPath(package){
  //I wanted to spend more time writting a comment about this "replace all" than making a decent function
  while(package.indexOf(".") > 0){
    package = package.replace(".", "/");
  }
  return package;
}

function getComponentName(file){
  var nameInit = file.lastIndexOf("/") + 1
  file = file.substring(nameInit)
  var fileTypeIndex = file.indexOf(".js")
  if(fileTypeIndex < 0){
    fileTypeIndex = file.indexOf(".jsx")
  }
  return file.substring(0, fileTypeIndex)
}

function toJavaType(type){
  switch(type) {
    case "string":
      return "String";
    default:
      //JavascriptFunction is a special type generated to enable handling client side events on the server
      return "JavascriptFunction"
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
