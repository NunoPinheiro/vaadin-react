"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
var React = require("react");

var TestComponent = exports.TestComponent = React.createClass({
  displayName: "TestComponent",

  propTypes: {
    element: React.PropTypes.element,
    message: React.PropTypes.string.isRequired
  },
  getInitialState: function getInitialState() {
    return {};
  },
  render: function render() {
    return React.createElement(
      "div",
      null,
      this.props.message,
      this.props.element
    );
  }
});

//HACK to enable view in browser
if (typeof window != "undefined") {
  window.TestComponent = TestComponent;
}