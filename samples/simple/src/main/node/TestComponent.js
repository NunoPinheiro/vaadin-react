"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
var React = require("react");

var TestComponent = exports.TestComponent = React.createClass({
  displayName: "TestComponent",

  propTypes: {
    name: React.PropTypes.string.isRequired,
    onNameChange: React.PropTypes.func.isRequired
  },
  getInitialState: function getInitialState() {
    return { "name": this.props.name };
  },
  render: function render() {
    return React.createElement(
      "div",
      null,
      React.createElement(
        "h1",
        null,
        "Hello, ",
        this.state.name
      ),
      "Change Name :",
      React.createElement("input", { type: "text", onChange: this.nameChanged }),
      React.createElement(
        "button",
        { onClick: this.triggerNameChangeEvent },
        "Click to change name on server"
      )
    );
  },
  nameChanged: function nameChanged(e) {
    if (this.state.name != e.target.value) {
      this.setState({ "name": e.target.value });
    }
  },
  triggerNameChangeEvent: function triggerNameChangeEvent(e) {
    this.props.onNameChange(this.state.name);
  }
});

//HACK to enable view in browser
if (typeof window != "undefined") {
  window.TestComponent = TestComponent;
}