package com.nunopinheiro.vaadin_react;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
		
		Label label = new Label("Write your markup in this Vaadin component, click on the button to submit it and it will be rendered on the React markdown component");
		TextArea textArea = new TextArea();
		
		ReactMarkdownImpl reactMarkdown = new ReactMarkdownImpl("");
		Button button = new Button("Click to Preview");
		//Update the React Markdown component with the markdown written in the Vaadin textarea
		button.addClickListener((x) ->reactMarkdown.setSource(textArea.getValue()));

		
		layout.addComponent(label);
		layout.addComponent(textArea);
		layout.addComponent(button);
		layout.addComponent(reactMarkdown);
		
		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}

	private void handle(JsonArray args){
		System.out.println("this will break client side because the component expects a return");
	}
	
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
