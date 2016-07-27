package com.nunopinheiro.vaadin_react;

import javax.servlet.annotation.WebServlet;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import elemental.json.Json;
import elemental.json.JsonArray;


/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("com.nunopinheiro.vaadin_react.MyAppWidgetset")
public class MyUI extends UI {
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();
		TestComponentImpl outerContainer = new TestComponentImpl("Outer");
		TestComponentImpl innerContainer = new TestComponentImpl("Inner");

		outerContainer.setElement(innerContainer);
		Button incOuter = new Button("Increment outer");
		Button incinner = new Button("Increment inner");
		incOuter.addClickListener(new IncClickListener(outerContainer, "outer"));
		incinner.addClickListener(new IncClickListener(innerContainer, "inner"));
		
		layout.addComponent(outerContainer);
		layout.addComponent(incOuter);
		layout.addComponent(incinner);
		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}
	
	class IncClickListener implements Button.ClickListener{
		TestComponentImpl component;
		String text;
		int val = 0;
		public IncClickListener(TestComponentImpl component, String text){
			this.text = text;
			this.component = component;
			component.setMessage(text + " " +  val);
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			component.setMessage(text + " " +  ++val);
		}
		
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

}
