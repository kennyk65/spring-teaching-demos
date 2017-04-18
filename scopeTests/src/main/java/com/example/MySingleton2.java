package com.example;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public abstract class MySingleton2 {

	public double getValue() {
		return getOtherBean().getValue();
	}


	@Lookup("myPrototype2")
    protected abstract MyPrototype2 getOtherBean();}
