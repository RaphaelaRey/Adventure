package kamisado.client;

import javafx.scene.Scene;
import javafx.stage.Stage;
import kamisado.commonClasses.Spielbrett;

public class ClientView {
	
	private Model model;
	private Stage stage;
	protected Spielbrett spielbrett;
	
    // Konstruktor
	public ClientView(Stage stage, Model model) {
		this.stage = stage; 
		this.model = model;
		stage.setMinWidth(826);
		stage.setMinHeight(855);
		stage.setMaxWidth(826);
		stage.setMaxHeight(855);
		stage.setTitle("Kamisado");
		
		spielbrett = new Spielbrett(model);

    	
		Scene scene = new Scene(spielbrett.getPane());
        stage.setScene(scene);
	}
	
	// start und show Methoden
	public void start(){
		stage.show();
	}
	public void stop(){
		stage.hide();
	}

}






