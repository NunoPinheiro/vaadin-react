package com.nunopinheiro.vaadin_react;

import javax.servlet.annotation.WebServlet;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

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
		Label component = new Label("nha");
		component.setId("testy");
		innerContainer.setElement(new VaadinComponentWrapper(component));
		layout.addComponent(outerContainer);
		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

}
