package {{package}};

import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.shared.ui.JavaScriptComponentState;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;

public class {{ name }}_Base extends AbstractJavaScriptComponent{

	public {{ name }}_Base({{requiredArgList}}){
		//initialize required properties from constructor
		{{#requiredProps}}
			set{{upperName}}({{name}});
		{{/requiredProps}}
		{{#props}}
			{{#isFunction}}
				addFunction("{{name}}Handler", this::handle{{upperName}});
			{{/isFunction}}
		{{/props}}

	}

	@Override
	public {{name}}State getState(){
		return ({{name}}State) super.getState();
	}

	{{#props}}
		{{^isFunction}}
			public {{ type }} get{{upperName}}(){
				return getState().get{{upperName}}();
			}

			public void set{{upperName}}({{ type }} {{name}}){
				getState().set{{upperName}}({{name}});
			}
		{{/isFunction}}
	{{/props}}

	//Register function handler and getters and setters
	{{#props}}
		{{#isFunction}}

			public {{ type }} {{ name }};

			private void handle{{upperName}}(JsonArray args){
				get{{upperName}}().call(args);
			}

			public {{ type }} get{{upperName}}(){
				return {{name}};
			}

			public void set{{upperName}}({{ type }} {{name}}){
				this.{{name}} = {{name}};
			}
		{{/isFunction}}
	{{/props}}

	//Vaadin enforces the state to have an empty constructor
	public static class {{name}}State extends JavaScriptComponentState{
		//Only fields which are not functions can be present on the state. Functions will be represented by the handlers
		{{#props}}
			{{^isFunction}}
				private {{ type }} {{ name }};

				public {{ type }} get{{upperName}}(){
					return {{name}};
				}

				public void set{{upperName}}({{ type }} {{name}}){
					this.{{name}} = {{name}};
				}
			{{/isFunction}}
		{{/props}}
	}

	// For now we are avoiding an external library,
	// so we will generate a simple interface for each generated component to enable the function handlers

	public interface JavascriptFunction{
		public void call(JsonArray args);
	}
}
