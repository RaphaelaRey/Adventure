package kamisado.client;

import javafx.scene.Scene;
import javafx.stage.Stage;
import kamisado.commonClasses.Spielbrett;

/**
 * @author Raphaela Rey
 */

public class ClientView {
	
	private ClientModel clientModel;
	private Stage stage;
	protected Spielbrett spielbrett;
	
    // Konstruktor
	public ClientView(Stage stage, ClientModel clientModel) {
		this.stage = stage; 
		this.clientModel = clientModel;
		stage.setMinWidth(826);
		stage.setMinHeight(855);
		stage.setMaxWidth(826);
		stage.setMaxHeight(855);
		stage.setTitle("Kamisado");
		
		spielbrett = new Spielbrett(clientModel);
		clientModel.setSpielbrett(spielbrett);
		
		Scene scene = new Scene(spielbrett.getPane());
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






