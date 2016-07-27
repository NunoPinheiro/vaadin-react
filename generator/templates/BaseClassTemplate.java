package {{package}};

import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.shared.ui.JavaScriptComponentState;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;
import com.nunopinheiro.vaadin_react.ReactComponent;

abstract public class {{ name }}_Base extends ReactComponent{

	@Override
	public String getComponentType(){
		return "{{name}}";
	}

	public {{ name }}_Base({{requiredArgList}}){
		//initialize required properties from constructor
		{{#requiredProps}}
			set{{upperName}}({{name}});
		{{/requiredProps}}

	}

	@Override
	public {{name}}State getState(){
		return ({{name}}State) super.getState();
	}

	{{#props}}
		{{^isFunction}}
		{{^isElement}}
			public {{ type }} get{{upperName}}(){
				return getState().get{{upperName}}();
			}

			public void set{{upperName}}({{ type }} {{name}}){
				if(getReactParent() != null){
					getReactParent().markAsDirty();
				}
				getState().set{{upperName}}({{name}});
			}
		{{/isElement}}
		{{#isElement}}
			public ReactComponent {{ name }};
			public ReactComponent get{{upperName}}(){
				return {{name}};
			}

			public void set{{upperName}}(ReactComponent {{name}}){
				if(getReactParent() != null){
					getReactParent().markAsDirty();
				}
				if(this.{{name}} != null){
					//Clean up react parent from former child
					this.{{name}}.setReactParent(null);
				}
				this.{{name}} = {{name}};
				//Add this component as react parent of the other component, enabling client refreshes on child changes
				this.{{name}}.setReactParent(this);
				getState().set{{upperName}}(new ReactComponent.ReactComponentStateWrapper({{name}}.getState()));
				getState().set{{upperName}}ComponentType({{name}}.getComponentType());
			}
		{{/isElement}}
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
				if({{name}} != null){
					addFunction("{{name}}Handler", this::handle{{upperName}});
				}
			}
		{{/isFunction}}
	{{/props}}

	//Vaadin enforces the state to have an empty constructor
	public static class {{name}}State extends ReactComponent.ReactComponentState{
		//Only fields which are not functions can be present on the state. Functions will be represented by the handlers
		{{#props}}
			{{^isFunction}}
			{{^isElement}}
				private {{ type }} {{ name }};

				public {{ type }} get{{upperName}}(){
					return {{name}};
				}

				public void set{{upperName}}({{ type }} {{name}}){
					this.{{name}} = {{name}};
				}
			{{/isElement}}
			{{#isElement}}
				private ReactComponent.ReactComponentStateWrapper {{ name }};
				private String {{ name }}ComponentType;

				public ReactComponent.ReactComponentStateWrapper get{{upperName}}(){
					return {{name}};
				}

				public void set{{upperName}}(ReactComponentStateWrapper {{name}}){
					this.{{name}} = {{name}};
				}

				public String get{{upperName}}ComponentType(){
					return {{name}}ComponentType;
				}

				public void set{{upperName}}ComponentType(String {{name}}ComponentType){
					this.{{name}}ComponentType = {{name}}ComponentType;
				}
			{{/isElement}}
			{{/isFunction}}
		{{/props}}
	}
}
