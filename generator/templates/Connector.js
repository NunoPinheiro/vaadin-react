//We need to calculate the vaadin client side name of the package wich we do by replacing '.' by '_'
var package = "{{package}}";
while(package.indexOf(".") > 0){
  package = package.replace(".", "_");
}

var connectorName = package + "_" + "{{name}}Impl";
console.log(connectorName)
window[connectorName] = function() {
      var Component = React.createFactory({{name}});


      this.onStateChange = function() {
      	//add the rpc call to the component properties
        {{#props}}
      		{{#isFunction}}
            this.getState().onNameChange = this.{{name}}Handler
          {{/isFunction}}
        {{/props}}
        ReactDOM.render( Component(this.getState()), this.getElement());
    }
};
