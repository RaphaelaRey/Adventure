package kamisado.client.löschenfenster;

import kamisado.ServiceLocator;
import kamisado.commonClasses.Translator;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author carmen walser
 *
 */
public class LöschenfensterView {
	
	Stage löschenStage;
	protected Label LöschMeldung;
	protected TextField löschenNametxt;
	protected TextField löschenPWtxt;
	protected Button löschenBestätigen;
	protected HBox hbox;
	
	public LöschenfensterView(Stage löschenStage) {
		this.löschenStage=löschenStage;
		ServiceLocator sl=ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		
		VBox vbox = new VBox();

		LöschMeldung = new Label(t.getString("LöschMeldung"));
		
		löschenNametxt = new TextField(t.getString("Benutzername"));
		löschenPWtxt = new TextField(t.getString("Passwort"));
		löschenBestätigen = new Button(t.getString("ButtonBestätigen"));
		hbox = new HBox();
		hbox.getChildren().addAll(löschenNametxt, löschenPWtxt, löschenBestätigen);
		
		vbox.getChildren().addAll(LöschMeldung, hbox);
		vbox.setMargin(LöschMeldung, new Insets(25,25,25,25));
		vbox.setMargin(hbox, new Insets(25,25,25,25));
		
		Scene scene = new Scene(vbox);
		löschenStage.setScene(scene);
	}
	
	public void start() {
		löschenStage.show();
		
	}

	public void stop() {
		löschenStage.hide();
		
	}

}
