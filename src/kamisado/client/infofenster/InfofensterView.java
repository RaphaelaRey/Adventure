package kamisado.client.infofenster;

import kamisado.ServiceLocator;
import kamisado.commonClasses.Translator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author carmen walser
 *
 */
public class InfofensterView {
	
	private Stage stage;	
	protected Button ok = new Button("OK");
	ServiceLocator sl = ServiceLocator.getServiceLocator();
	Translator t = sl.getTranslator();	

	public InfofensterView(Stage primaryStage,Label label) {
		this.stage = primaryStage;
		
		stage.setTitle("INFORMATION");
		
		BorderPane bpane = new BorderPane();
		bpane.setCenter(label);
		bpane.setBottom(ok);
		bpane.setAlignment(ok, Pos.BOTTOM_CENTER);
		bpane.setMargin(ok, new Insets(25,25,25,25));
		bpane.setMargin(label, new Insets(15,15,15,15));		
						
		Scene scene = new Scene(bpane);
		stage.setScene(scene);
	}

	
	public void stop(){
		stage.hide();
	}
	
	public void start(){
		stage.show();
	}
	

}
