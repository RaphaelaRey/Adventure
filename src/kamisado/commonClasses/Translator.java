package kamisado.commonClasses;

import java.util.Locale;
import java.util.ResourceBundle;

import kamisado.ServiceLocator;

public class Translator {
	
	private ServiceLocator sl = ServiceLocator.getServiceLocator();
	
	protected Locale currentLocale;
	private ResourceBundle resourceBundle;
	

	public Translator(String localeString) {
		Locale locale=Locale.getDefault();
		if(locale!=null){
			Locale[]verfügbareLocales = sl.getLocales();
			for(int i = 0; i<verfügbareLocales.length;i++){
				String tmpLang = verfügbareLocales[i].getLanguage();
				if(localeString.substring(0,tmpLang.length()).equals(tmpLang)){
					locale=verfügbareLocales[i];
				break;
				}
			}
		}
		resourceBundle=ResourceBundle.getBundle(sl.getApp_CLASS().getName(), locale);
		Locale.setDefault(locale);
		currentLocale=locale;
	}
	
	public Locale getCurrentLocale(){
		return currentLocale;
	}
	
	public String getString(String key){
		return resourceBundle.getString(key);
	}
}
