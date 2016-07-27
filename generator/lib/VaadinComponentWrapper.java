package com.nunopinheiro.vaadin_react;

import java.util.Iterator;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.Component;

//@JavaScript({"vaadinComponentWrapper.js", "https://fb.me/react-15.2.1.min.js", "https://fb.me/react-dom-15.2.1.min.js"})
@JavaScript({"https://fb.me/react-15.2.1.js", "https://fb.me/react-dom-15.2.1.js", "vaadinComponentWrapper.js"})
//TODO we should split this into an abstract and impl to enable js resources optimization
public class VaadinComponentWrapper extends ReactComponent{


  Component component;
    public VaadinComponentWrapper(Component component){
      component.setParent(this);
      getState().setComponent(component);
    }

    public void setComponent(Component component){
      this.component = component;
    }

    public Component getComponent(){
      return component;
    }

	@Override
	public Iterator<Component> iterator() {
		return new java.util.LinkedList<Component>().iterator();
	}

	@Override
	public String getComponentType() {
		return "VaadinComponentWrapper";
	}

	public VaadinComponentWrapperState getState(){
		return (VaadinComponentWrapperState) super.getState();
	}

	public static class VaadinComponentWrapperState extends ReactComponentState{
		private Component component;

    //The connectorID needs to be lazy loaded to ensure the component is already attached to the UI
		public String getConnector() {
			return component.getConnectorId();
		}

		public void setConnector(String connector) {
			throw new RuntimeException("Use setComponent with the component instead of using the connector");
		}

		public void setComponent(Component component) {
			this.component = component;
		}
	}
}
