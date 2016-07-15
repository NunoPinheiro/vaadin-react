var React = require("react");
export var TestComponent = React.createClass({
  propTypes: {
   name : React.PropTypes.string.isRequired,
   onNameChange : React.PropTypes.func.isRequired
  },
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



//HACK to enable view in browser
if(typeof window != "undefined"){
  window.TestComponent = TestComponent;
}
