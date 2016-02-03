package com.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class BPP implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object o, String s)
			throws BeansException {
		System.out.println( "BPP after " + o.getClass().getSimpleName() );
		return o;
	}

	@Override
	public Object postProcessBeforeInitialization(Object o, String s)
			throws BeansException {
		System.out.println( "BPP before " + o.getClass().getSimpleName() );
		return o;
	}

	
}
