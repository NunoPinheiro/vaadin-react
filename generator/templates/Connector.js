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

window[connectorName] = function() {
      var Component = React.createFactory({{name}});

      this.onStateChange = function() {

        var props = {};
        //add the rpc call to the component properties
        {{#props}}
      		{{#isFunction}}
            props.{{name}} = this.{{name}}Handler
          {{/isFunction}}
        {{/props}}
        //add the non function elements
        {{#props}}
          {{^isFunction}}
            props.{{name}} = this.getState().{{name}}
          {{/isFunction}}
        {{/props}}

        ReactDOM.render( Component(props), this.getElement());
    }
};
