package kamisado.client.anmeldefenster;

import kamisado.ServiceLocator;
import kamisado.client.ClientModel;
import kamisado.client.ClientView;
import kamisado.commonClasses.Translator;

import java.util.Locale;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
	private AnmeldefensterController anmeldeController;
	private ClientModel clientModel;
	private ClientView clientView;
	
	protected MenuBar menuBar;
	protected Menu menuDatei;
	public MenuItem menuDateiAbmelden;
	public MenuItem menuDateiLöschen;
	protected Menu menuOptionen;
	protected Menu menuOptionenSprache;
	protected Menu menuHilfe;
	public MenuItem menuHilfeRegeln;
	
	public Label einstellungSprache;
	public Label login;
	public TextField anmeldenNametxt;
	public TextField anmeldenPwtxt;
	public TextField anmeldenIPtxt;
	public Button btnAnmelden;
	public Label neuregistrierenLabel;
	public TextField registrierenNametxt;
	public TextField registrierenPwtxt;
	public Button btnRegistrieren;
	
	public Label regeln;
	public Label startMeldung;
	public Label PWfalschMeldung;
	public Label BenutzerExistiertNicht;
	public Label BenutzernameVergeben;
	public Label RegistrierMeldung;
	public Label PasswortZuKurz;
	
	public AnmeldefensterView(Stage primaryStage, AnmeldefensterController anmeldeController, ClientModel clientModel, ClientView clientView) {
		this.stage=primaryStage;
		this.anmeldeController= anmeldeController;
		this.clientModel=clientModel;
		this.clientView=clientView;
		ServiceLocator sl=ServiceLocator.getServiceLocator();
		//sl.setTranslator(new Translator("de"));
		Translator t = sl.getTranslator();
		
		primaryStage.setTitle(t.getString("Anmeldung"));
		
		GridPane gpane=new GridPane();
		
		menuBar = new MenuBar();
		menuOptionen=new Menu(t.getString("MenuOptionen"));
		menuOptionenSprache=new Menu(t.getString("MenuSprache"));
		menuOptionen.getItems().add(menuOptionenSprache);
								
		for (Locale locale : sl.getLocales()) {
			MenuItem sprache = new MenuItem(locale.getLanguage());
			menuOptionenSprache.getItems().add(sprache);
			sprache.setOnAction(event -> {
				sl.setTranslator(new Translator(locale.getLanguage()));
				clientView.updateTexts();
				updateTexts();
			});
		}	
		menuHilfe=new Menu(t.getString("MenuHilfe"));
		menuHilfeRegeln=new MenuItem(t.getString("MenuRegeln"));
		menuHilfe.getItems().add(menuHilfeRegeln);
		
		menuDatei = new Menu(t.getString("MenuDatei"));
		menuDateiAbmelden = new MenuItem(t.getString("MenuAbmelden"));
		menuDateiLöschen = new MenuItem(t.getString("MenuLöschen"));
		menuDatei.getItems().addAll(menuDateiAbmelden, menuDateiLöschen);
		menuDateiAbmelden.setDisable(true);
		menuDateiLöschen.setDisable(true);
		
		regeln = new Label(t.getString("Regeln"));
		startMeldung = new Label(t.getString("StartMeldung"));
		PWfalschMeldung = new Label(t.getString("PWfalschMeldung"));
		BenutzerExistiertNicht = new Label(t.getString("BenutzerExistiertNicht"));
		BenutzernameVergeben = new Label(t.getString("BenutzernameVergeben"));
		RegistrierMeldung = new Label(t.getString("RegistrierMeldung"));
		PasswortZuKurz = new Label(t.getString("PasswortZuKurz"));
		
		menuBar.getMenus().addAll(menuDatei, menuOptionen, menuHilfe);
		
		
		einstellungSprache = new Label(t.getString("EinstellungSprache"));
				
		login = new Label(t.getString("Login"));
		
		HBox hbox = new HBox();
		anmeldenNametxt=new TextField(t.getString("Benutzername"));
		anmeldenPwtxt = new TextField(t.getString("Passwort"));
		anmeldenIPtxt = new TextField(clientModel.getIP());
		btnAnmelden = new Button(t.getString("ButtonAnmelden"));
		hbox.getChildren().addAll(anmeldenNametxt, anmeldenPwtxt,anmeldenIPtxt, btnAnmelden);
		
		neuregistrierenLabel = new Label(t.getString("Neuregistrieren"));
		
		HBox hbox2 = new HBox();
		registrierenNametxt = new TextField(t.getString("Benutzername"));
		registrierenPwtxt = new TextField(t.getString("PasswortLänge"));
		btnRegistrieren =new Button(t.getString("ButtonRegistrieren"));
		hbox2.getChildren().addAll(registrierenNametxt, registrierenPwtxt, btnRegistrieren);
		
		gpane.add(menuBar, 0, 0);
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
	
	public void updateTexts() {
		Translator t=ServiceLocator.getServiceLocator().getTranslator();
		menuOptionen.setText(t.getString("MenuOptionen"));
		menuOptionenSprache.setText(t.getString("MenuSprache"));
		einstellungSprache.setText(t.getString("EinstellungSprache"));
		login.setText(t.getString("Login"));
		anmeldenNametxt.setText(t.getString("Benutzername"));
		anmeldenPwtxt.setText(t.getString("Passwort"));
		btnAnmelden.setText(t.getString("ButtonAnmelden"));
		neuregistrierenLabel.setText(t.getString("Neuregistrieren"));
		registrierenPwtxt.setText(t.getString("PasswortLänge"));
		btnRegistrieren.setText(t.getString("ButtonRegistrieren"));
		menuHilfe.setText(t.getString("MenuHilfe"));
		menuHilfeRegeln.setText(t.getString("MenuRegeln"));
		menuDatei.setText(t.getString("MenuDatei"));
		menuDateiAbmelden.setText(t.getString("MenuAbmelden"));
		menuDateiLöschen.setText(t.getString("MenuLöschen"));
		anmeldenIPtxt.setText(t.getString("IP"));
		
		startMeldung.setText(t.getString("StartMeldung"));
		PWfalschMeldung.setText(t.getString("PWfalschMeldung"));
		BenutzerExistiertNicht.setText(t.getString("BenutzerExistiertNicht"));
		BenutzernameVergeben.setText(t.getString("BenutzernameVergeben"));
		RegistrierMeldung.setText(t.getString("RegistrierMeldung"));
		PasswortZuKurz.setText(t.getString("PasswortZuKurz"));
		regeln.setText(t.getString("Regeln"));
	}

	public void start() {
		stage.show();
	}

	public void stop() {
		stage.hide();
	}

}
