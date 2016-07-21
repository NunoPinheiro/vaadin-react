//We need to calculate the vaadin client side name of the package wich we do by replacing '.' by '_'
var ReactDOM = require("react-dom");
var React = require("react");
var module = require("{{& file}}");

// The module can be exported either by its name or as the default export
var {{name}} = module.{{name}} || module;

var package = "{{package}}";
while(package.indexOf(".") > 0){
  package = package.replace(".", "_");
}

var connectorName = package + "_" + "{{name}}Impl";
var renderFunction = "$vaadinReact{{name}}Render";

window[connectorName] = function() {
      this.onStateChange = function() {
        var component = window[renderFunction](this.getState());
        ReactDOM.render(component, this.getElement());
    }
};

//The component can be rendered either from a top level (as a vaadin component), or within another vaadin component
window[renderFunction] = function(state) {
  var Component = React.createFactory({{name}});
  var props = {};
  //add the rpc call to the component properties
  {{#props}}
    {{#isFunction}}
      props.{{name}} = this.{{name}}Handler;
    {{/isFunction}}
  {{/props}}
  //add the non function elements
  {{#props}}
    {{^isFunction}}
      {{^isSymbol}}
      {{^isElement}}
        props.{{name}} = state.{{name}};
      {{/isElement}}
      {{/isSymbol}}
      {{#isSymbol}}
        props.{{name}} = Symbol(state.{{name}});
      {{/isSymbol}}
      {{#isElement}}
        if(state.{{name}}ComponentType){
          //Render an inner component
          var componentRenderer = window["$vaadinReact" + state.{{name}}ComponentType + "Render"];
          props.{{name}} = componentRenderer(state.{{name}});
        }
      {{/isElement}}
    {{/isFunction}}
  {{/props}}
  return Component(props);
}
