package com.nunopinheiro.vaadin_react

import com.vaadin.ui.AbstractJavaScriptComponent
import com.vaadin.annotations.JavaScript
import com.vaadin.shared.ui.JavaScriptComponentState
import com.vaadin.ui.JavaScriptFunction
import elemental.json.JsonArray

@JavaScript("test.js", "generated/react.min.js", "generated/TestComponent.js", "generated/react-dom.min.js")
class TestComponent : AbstractJavaScriptComponent{
	
	constructor(name : String){
		setName(name)
		addFunction("nameChanged", {nameChanged(it)});
	}
	
	fun nameChanged(args : JsonArray){
		var newName = args.getString(0)
		println(newName)
		setName(newName)
	}
	
	fun setName(name : String){
		getState().name = name
	}
	
	override fun getState() : TestComponentState{
		return super.getState() as TestComponentState
	}
	
	
}

//Vaadin enforces the state to have an empty constructor
class TestComponentState() : JavaScriptComponentState() {
	var name : String = ""
	constructor(name : String) : this(){
		this.name = name
	}
    
}