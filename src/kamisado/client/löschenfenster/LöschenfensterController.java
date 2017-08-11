package kamisado.client.löschenfenster;

import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import kamisado.ServiceLocator;
import kamisado.client.ClientModel;
import kamisado.client.ClientView;
import kamisado.client.infofenster.InfofensterController;
import kamisado.client.infofenster.InfofensterView;
import kamisado.commonClasses.Translator;

/**
 * @author carmen walser
 *
 */
public class LöschenfensterController {
	
	final private LöschenfensterView löschenView;
	private ClientView clientView;
	private ClientModel clientModel;

	public LöschenfensterController(LöschenfensterView löschenView, ClientView clientView, ClientModel clientModel) {
		this.löschenView = löschenView;
		this.clientView=clientView;
		this.clientModel=clientModel;
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		
		löschenView.löschenBestätigen.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				String name = löschenView.löschenNametxt.getText();
				String pw = löschenView.löschenPWtxt.getText();
				String art = "löschen";
				
				clientModel.AnmeldungSenden(art, name, pw);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String meldung = clientModel.getMeldung();
				
				if(meldung.equals("ErfolgreichMeldung")){
					Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,clientView.erfolgreichMeldung);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true);
					löschenView.stop();
				}
				if(meldung.equals("BenutzerExistiertNichtMeldung")){
					Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,clientView.benutzerExistiertNichtMeldung);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true);
				}
				
				if(meldung.equals("PasswortFalschMeldung")){
					Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,clientView.passwortFalschMeldung);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true);
				}
			}
			
		});
		
	}

}
