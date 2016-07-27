var renderFunction = "$vaadinReactVaadinComponentWrapperRender";
var connectorName =  "com_nunopinheiro_vaadin_react_VaadinComponentWrapper";
window[connectorName] = function() {
    this.onStateChange = function() {
      var component = window[renderFunction](this.getState());
      ReactDOM.render(component, this.getElement());
      debugger;
    }
};

var VaadinComponentWrapper = React.createClass({
  propTypes: {
    connector: React.PropTypes.string.isRequired
  },
  render: function render() {
    return React.createElement(
      "div");
  }
});

window[renderFunction] = function(){
  var Component = React.createElement(VaadinComponentWrapper);
  return Component;
}
