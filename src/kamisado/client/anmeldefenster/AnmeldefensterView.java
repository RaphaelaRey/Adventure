package kamisado.client.anmeldefenster;

import kamisado.ServiceLocator;
import kamisado.commonClasses.Translator;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author carmen walser
 *
 */
public class AnmeldefensterView {
	
	private Stage stage;
	
	protected Label einstellungSprache;
	protected Label login;
	protected TextField anmeldenNametxt;
	protected TextField anmeldenPwtxt;
	protected Button btnAnmelden;
	protected Label neuregistrierenLabel;
	protected TextField registrierenNametxt;
	protected TextField registrierenPwtxt;
	protected Button btnRegistrieren;
	
	public AnmeldefensterView(Stage primaryStage) {
		this.stage=primaryStage;
		ServiceLocator sl=ServiceLocator.getServiceLocator();
		sl.setTranslator(new Translator("de"));
		Translator t = sl.getTranslator();
		
		primaryStage.setTitle(t.getString("Anmeldung"));
		
		GridPane gpane=new GridPane();
		
		einstellungSprache = new Label(t.getString("EinstellungSprache"));
				
		login = new Label(t.getString("Login"));
		
		HBox hbox = new HBox();
		anmeldenNametxt=new TextField(t.getString("Benutzername"));
		anmeldenPwtxt = new TextField(t.getString("Passwort"));
		btnAnmelden = new Button(t.getString("ButtonAnmelden"));
		hbox.getChildren().addAll(anmeldenNametxt, anmeldenPwtxt, btnAnmelden);
		
		neuregistrierenLabel = new Label(t.getString("Neuregistrieren"));
		
		HBox hbox2 = new HBox();
		registrierenNametxt = new TextField(t.getString("Benutzername"));
		registrierenPwtxt = new TextField(t.getString("PasswortLänge"));
		btnRegistrieren =new Button(t.getString("ButtonRegistrieren"));
		hbox2.getChildren().addAll(registrierenNametxt, registrierenPwtxt, btnRegistrieren);
		
		gpane.add(einstellungSprache, 0, 1);
		gpane.add(login,0,3);
		gpane.add(hbox, 0, 4);
		gpane.add(neuregistrierenLabel, 0, 5);
		gpane.add(hbox2, 0, 6);
		
		GridPane.setMargin(einstellungSprache, new Insets(15));
		GridPane.setMargin(login, new Insets(15));
		GridPane.setMargin(hbox, new Insets(15));
		GridPane.setMargin(neuregistrierenLabel, new Insets(15));
		GridPane.setMargin(hbox2, new Insets(15));
		
		Scene scene = new Scene(gpane);
		stage.setScene(scene);
		
	}
	
	public void start() {
		stage.show();
	}

	public void stop() {
		stage.hide();
	}

}
