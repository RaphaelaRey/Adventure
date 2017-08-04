package kamisado.client;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kamisado.commonClasses.Spielbrett;


public class ClientView {
	
	private ClientModel clientModel;
	private Stage stage;
	protected Spielbrett spielbrett;
	
    // Konstruktor
	public ClientView(Stage stage, ClientModel clientModel) {
		this.stage = stage; 
		this.clientModel = clientModel;
			
		spielbrett = new Spielbrett(clientModel);
		clientModel.setSpielbrett(spielbrett);
		
		stage.setTitle("Kamisado");
		stage.setMinWidth(spielbrett.SPIELBRETTBREITE);
		stage.setMaxWidth(spielbrett.SPIELBRETTBREITE);
		stage.setMinHeight(spielbrett.SPIELBRETTHÖHE);
		stage.setMaxHeight(spielbrett.SPIELBRETTHÖHE);
		
		BorderPane borderPane = new BorderPane();
		Button button = new Button("Menuleiste"); // TODO Carmen durch Menuleiste ersetzen (habe Gridpane bereits in Borderpane hinzugefügt)
		borderPane.setTop(button); // durch Menuleiste ersetzen
		borderPane.setCenter(spielbrett.getPane());
		
		Scene scene = new Scene(borderPane);
        stage.setScene(scene);
	}
	
	// Start und show Methoden
	public void start(){
		stage.show();
	}
	public void stop(){
		stage.hide();
	}
}