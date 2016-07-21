var React = require("react");

export var TestComponent = React.createClass({
  propTypes: {
   element : React.PropTypes.element,
   message : React.PropTypes.string.isRequired
  },
  getInitialState : function(){
    return {}
  },
  render : function(){
    return <div>
              {this.props.message}
              {this.props.element}
           </div>;
  }
});



//HACK to enable view in browser
if(typeof window != "undefined"){
  window.TestComponent = TestComponent;
}
