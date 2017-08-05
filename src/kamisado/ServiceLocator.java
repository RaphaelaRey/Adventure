package kamisado;

import java.util.Locale;

import kamisado.commonClasses.Translator;

/**
 *Copyright 2015, FHNW, Prof. Dr. Brad Richards All rights reserved.
 * @author Bradley Richards
 */
public class ServiceLocator {
	
	private static ServiceLocator serviceLocator;
	
	final private Class<?> APP_CLASS = Kamisado.class;
	final private String APP_NAME = APP_CLASS.getSimpleName();
	
	final private Locale[] locales = new Locale[]{new Locale("de"), new Locale("en")};
	
	private Translator translator;
	
	public static ServiceLocator getServiceLocator() {
		if(serviceLocator == null)
			serviceLocator= new ServiceLocator();
		return serviceLocator;
	}
	
	private ServiceLocator(){
	}
	
	public Class<?> getApp_CLASS(){
		return APP_CLASS;
	}
	
	public String getAPP_NAME(){
		return APP_NAME;
	}
	
	public Locale[] getLocales(){
		return locales;
	}
	
	public Translator getTranslator(){
		return translator;
	}
	
	public void setTranslator(Translator translator){
		this.translator = translator;
	}

}
