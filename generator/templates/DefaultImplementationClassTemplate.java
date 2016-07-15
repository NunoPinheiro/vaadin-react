package {{package}};

import com.vaadin.annotations.JavaScript;

/**
  This is the default generated class, you can directly use this class to instantiate your component
  Notice that the resources referred in the @Javascript tag will only be loaded after vaadin needs to present the component
  If you always use this component (and use it on your entry view) you have several options you can add the resources to you UI using the @JavaScript annotation

  If you are using vaadin embedded in a page, you may also opt to add the resources to the external page, so when vaadin needs to load the components they are already present.
  In that case, instead of this component, you should create a new class which inherits from {{ name }}_Base and use it instead
**/
@JavaScript({"{{name}}Connector.js", "react.min.js", "react-dom.min.js", "{{name}}.js"})
public class {{ name }}Impl extends {{name}}_Base{
  public {{ name }}Impl({{requiredArgList}}){
    super({{requiredArgListCall}});
  }
}
