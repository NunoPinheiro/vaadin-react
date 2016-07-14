var React = require("react");
var TestComponent = React.createClass({
  getInitialState : function(){
    return {"name" : this.props.name}
  },
  render : function(){
    return <div>
              <h1>Hello, {this.state.name}</h1>
              Change Name :
              <input type="text" onChange={this.nameChanged} />
              <button onClick={this.triggerNameChangeEvent}></button>
           </div>;
  },
  nameChanged : function(e){
    if(this.state.name != e.target.value){
      this.setState({"name" : e.target.value})
    }
  },
  triggerNameChangeEvent : function(e){
    this.props.onNameChange(this.state.name)
  }
});

export default TestComponent;

//HACK to enable view in browser
window.TestComponent = TestComponent;
