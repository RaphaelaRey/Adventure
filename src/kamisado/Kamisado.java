package kamisado;


import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import kamisado.client.ClientController;
import kamisado.client.ClientView;
import kamisado.client.anmeldefenster.AnmeldefensterView;
import kamisado.client.ClientModel;

public class Kamisado extends Application {
	
	private ClientView view;
	private ClientController controller;
	private ClientModel clientModel;
	private AnmeldefensterView anmeldeView;
		
	public static void main(String[] args) {
		launch(args);	
	}

	@Override
	public void start(Stage primaryStage) throws IOException{
		clientModel = new ClientModel();
		view = new ClientView(primaryStage, clientModel, anmeldeView);
		controller = new ClientController(clientModel, view);	
		view.start();
	}
	
	@Override
	public void stop(){
		if (view!=null)
			view.stop();
	}
	

}
