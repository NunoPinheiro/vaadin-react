package com.nunopinheiro.vaadin_react;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import com.vaadin.server.JsonCodec;
import com.vaadin.server.communication.JSONSerializer;
import com.vaadin.ui.ConnectorTracker;
import elemental.json.JsonValue;
import elemental.json.Json;
import elemental.json.JsonObject;
import com.vaadin.server.EncodeResult;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
  This class is copied to the target folder to avoid distributing the project in maven.
**/
public abstract class ReactComponent extends AbstractJavaScriptComponent{

  //Turn the method into public
  public ReactComponentState getState(){
    return (ReactComponentState) super.getState();
  }

  /**
    Returns the type of the current component.
    Is required in the connector to enable renderization of embedded ReactComponents
  **/
  public abstract String getComponentType();

  //During react components composition we need to track the parent to allow updating the client-side state
  private ReactComponent reactParent;

	public ReactComponent getReactParent(){
		return reactParent;
	}

	protected void setReactParent(ReactComponent reactParent){
		this.reactParent = reactParent;
	}

 @Override
 public void markAsDirty() {
		if(getReactParent() != null){
			getReactParent().markAsDirty();
		}
		else{
			super.markAsDirty();
		}
	}

  /**
    Magic going on here to enable  inner components.
    Hack 1:
    Vaadin serializes the javascript component state based on the type of the getter.
    If we add a ReactComponent inside another ReactComponent vaadin won't be able to calculate the right type,
    and serialize only the fields which exist on the supertype (ReactComponentState).
    To enable this we are hacking into vaadin internals to inject a custom serializer (CustomSerializer).
    This serializer delegates the serialization to Vaadin, but uses the concrete type of the state instead of the type on the signature.

    Hack 2:
    Using hack 1 to serialize the states vaadin won't be able to calculate a diff and the state of the object will be null (check JsonCodec.encode()@ vaadin-server)
    To enable our hack 1 we can only customly serialize inner components, not the top level one being rendered.
    To enable this, we created a ReactComponentStateWrapper.

    These hacks use lots of private methods, and can be broken in the future.
  **/
  static{
    initCustomSerializer();
  }

  public static class ReactComponentState extends com.vaadin.shared.ui.JavaScriptComponentState{}

  public static class ReactComponentStateWrapper implements java.io.Serializable{
    private ReactComponentState state;

    public ReactComponentStateWrapper(ReactComponentState state){
      setState(state);
    }
    public ReactComponentState getState(){

      return state;
    }

    public void setState(ReactComponentState state){
      this.state = state;
    }
  }


	private static class CustomSerializer implements JSONSerializer<ReactComponentStateWrapper> {
		static final Method defaultSerializerMethod = setupSerializationMethod();
		@Override

		public ReactComponentStateWrapper deserialize(Type type, JsonValue jsonValue, ConnectorTracker connectorTracker) {
			throw new RuntimeException("Unable to deserialize components");
		}

		@Override
		public JsonValue serialize(ReactComponentStateWrapper value, ConnectorTracker connectorTracker) {
			try {
        EncodeResult result = (EncodeResult) defaultSerializerMethod.invoke(null, value.getState(), value.getState().getClass(), null, connectorTracker);
        return result.getEncodedValue();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new RuntimeException("Unable to serialize value", e);
			}
		}
	}

	private static Method setupSerializationMethod() {
		Method defaultSerializerMethod;
		try {
			defaultSerializerMethod = JsonCodec.class.getDeclaredMethod("encodeObject", Object.class, Class.class, JsonObject.class,
					ConnectorTracker.class);
			defaultSerializerMethod.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Unable to setup serialization methods", e);
		}
		return defaultSerializerMethod;
	}


	public static void initCustomSerializer() {
		try {
			final Field serializersField = Class.forName("com.vaadin.server.JsonCodec").getDeclaredField("customSerializers");
			serializersField.setAccessible(true);

			Map<Class<?>, JSONSerializer<?>> serializers = (Map<Class<?>, JSONSerializer<?>>) serializersField
					.get(null);

			if (!serializers.keySet().contains(ReactComponentStateWrapper.class)) {
				serializers.put(ReactComponentStateWrapper.class, new CustomSerializer());
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | ClassNotFoundException e) {
			throw new RuntimeException("Unable to initialize custom serializer for JavascriptComponentState.", e);
		}
	}

}
