window.com_nunopinheiro_vaadin_react_TestComponent =
    function() {
      var Component = React.createFactory(TestComponent);

      this.onStateChange = function() {
    	//add the rpc call to the component properties
	    this.getState().onNameChange = this.nameChanged

        ReactDOM.render( Component(this.getState()), this.getElement());
    }
    };
