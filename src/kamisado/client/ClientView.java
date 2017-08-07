package kamisado.client;

import java.util.Locale; 

import kamisado.ServiceLocator;
import kamisado.client.anmeldefenster.AnmeldefensterController;
import kamisado.client.anmeldefenster.AnmeldefensterView;
import kamisado.commonClasses.Translator;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kamisado.commonClasses.Spielbrett;


public class ClientView {
	
	private ClientModel clientModel;
	private Stage stage;
	protected Spielbrett spielbrett;
	private AnmeldefensterView anmeldeView;
	private AnmeldefensterController anmeldeController;
			
	protected MenuBar menuBar;
	protected Menu menuDatei;
	public MenuItem menuDateiAbmelden;
	public MenuItem menuDateiLöschen;
	protected Menu menuOptionen;
	protected Menu menuOptionenSprache;
	protected Menu menuHilfe;
	public MenuItem menuHilfeRegeln;
	
	public Label BlockadeMeldungSchwarz;
	public Label BlockadeMeldungWeiss;
	public Label GewinnerMeldungStillstandWeiss;
	public Label GewinnerMeldungStillstandSchwarz;
	public Label GewinnerMeldungSchwarz;
	public Label GewinnerMeldungWeiss;
	
    // Konstruktor
	public ClientView(Stage stage, ClientModel clientModel, AnmeldefensterView anmeldeView) {
		this.stage = stage; 
		this.clientModel = clientModel;
		this.anmeldeView = anmeldeView;
				
		ServiceLocator sl=ServiceLocator.getServiceLocator();
		sl.setTranslator(new Translator("de"));
		Translator t = sl.getTranslator();
					
		spielbrett = new Spielbrett(clientModel);
		clientModel.setSpielbrett(spielbrett);
		
		stage.setTitle("Kamisado");
		stage.setMinWidth(spielbrett.SPIELBRETTBREITE);
		stage.setMaxWidth(spielbrett.SPIELBRETTBREITE);
		stage.setMinHeight(spielbrett.SPIELBRETTHÖHE);
		stage.setMaxHeight(spielbrett.SPIELBRETTHÖHE);
		
		BorderPane borderPane = new BorderPane();
		
		menuBar = new MenuBar();
		menuOptionen=new Menu(t.getString("MenuOptionen"));
		menuOptionenSprache=new Menu(t.getString("MenuSprache"));
		menuOptionen.getItems().add(menuOptionenSprache);
								
		for (Locale locale : sl.getLocales()) {
			MenuItem sprache = new MenuItem(locale.getLanguage());
			menuOptionenSprache.getItems().add(sprache);
			sprache.setOnAction(event -> {
				sl.setTranslator(new Translator(locale.getLanguage()));
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
					
		menuBar.getMenus().addAll(menuDatei, menuOptionen, menuHilfe);
		
		borderPane.setTop(menuBar);
		
		BlockadeMeldungSchwarz = new Label(t.getString("BlockadeMeldungSchwarz"));
		BlockadeMeldungWeiss = new Label(t.getString("BlockadeMeldungWeiss"));
		GewinnerMeldungStillstandWeiss = new Label(t.getString("GewinnerMeldungStillstandWeiss"));
		GewinnerMeldungStillstandSchwarz = new Label(t.getString("GewinnerMeldungStillstandSchwarz"));
		GewinnerMeldungSchwarz = new Label(t.getString("GewinnerMeldungSchwarz"));
		GewinnerMeldungWeiss = new Label(t.getString("GewinnerMeldungWeiss"));
		
		borderPane.setCenter(spielbrett.getPane());
		
		Scene scene = new Scene(borderPane);
        stage.setScene(scene);
                  
        Stage neueStage = new Stage();
        neueStage.setAlwaysOnTop(true);
        AnmeldefensterView neueView = new AnmeldefensterView(neueStage, anmeldeController, clientModel, this);
        AnmeldefensterController anmeldeController = new AnmeldefensterController(neueView, this, clientModel);
        neueView.start();
	}
	
	// Start und show Methoden
	public void start(){
		stage.show();
	}
	public void stop(){
		stage.hide();
	}
	
	public void updateTexts(){
		Translator t=ServiceLocator.getServiceLocator().getTranslator();
		menuOptionen.setText(t.getString("MenuOptionen"));
		menuOptionenSprache.setText(t.getString("MenuSprache"));
		menuHilfe.setText(t.getString("MenuHilfe"));
		menuHilfeRegeln.setText(t.getString("MenuRegeln"));
		menuDatei.setText(t.getString("MenuDatei"));
		menuDateiAbmelden.setText(t.getString("MenuAbmelden"));
		menuDateiLöschen.setText(t.getString("MenuLöschen"));
		BlockadeMeldungSchwarz.setText(t.getString("BlockadeMeldungSchwarz"));
		BlockadeMeldungWeiss.setText(t.getString("BlockadeMeldungWeiss"));
		GewinnerMeldungStillstandWeiss.setText(t.getString("GewinnerMeldungStillstandWeiss"));
		GewinnerMeldungStillstandSchwarz.setText(t.getString("GewinnerMeldungStillstandSchwarz"));
		GewinnerMeldungSchwarz.setText(t.getString("GewinnerMeldungSchwarz"));
		GewinnerMeldungWeiss.setText(t.getString("GewinnerMeldungWeiss"));
		
		}
}