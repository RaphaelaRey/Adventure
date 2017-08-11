package kamisado.client.anmeldefenster;

import java.io.BufferedReader;   
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
import kamisado.client.löschenfenster.LöschenfensterController;
import kamisado.client.löschenfenster.LöschenfensterView;
import kamisado.commonClasses.Translator;

/**
 * @author carmen walser
 *
 */
public class AnmeldefensterController {
	
	private AnmeldefensterView anmeldeView;
	private ClientView clientView;
	private ClientModel clientModel;
	
	private static String name;
	private static String pw;
	private static String ip;
	
	public AnmeldefensterController(AnmeldefensterView anmeldeView, ClientView clientView, ClientModel clientModel) {
		this.anmeldeView = anmeldeView;
		this.clientView= clientView;
		this.clientModel=clientModel;
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		
		anmeldeView.btnAnmelden.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				name=anmeldeView.anmeldenNametxt.getText();
				pw=anmeldeView.anmeldenPwtxt.getText();
				ip=anmeldeView.anmeldenIPtxt.getText();
				String art = "anmelden";
				
				clientModel.Verbinden(ip, name, pw, art);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				String meldung = clientModel.getMeldung();
				//Wenn Eingaben stimmen, erscheint Fenster mit Startmeldung
				if(meldung.equals("startMeldung")){
					Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,anmeldeView.startMeldung);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true);
					anmeldeView.stop();
					clientView.menuDateiAbmelden.setDisable(false);
					clientView.menuDateiLöschen.setDisable(false);
				}
				//Wenn das Passwort falsch ist, erscheint die Meldung, dass das Passwort falsch ist
				if(meldung.equals("PasswortFalsch")){
					Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,anmeldeView.PWfalschMeldung);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true);
				}
				//Wenn der Benutzername im File nicht gespeichert ist, erscheint die Meldung, dass der Benutzer nicht existiert
				if(meldung.equals("BenutzerExistiertNicht")){
					Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,anmeldeView.BenutzerExistiertNicht);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true);
				}
			}
			
		});
		
		anmeldeView.btnRegistrieren.setOnAction(new EventHandler<ActionEvent> (){
			public void handle(ActionEvent e2){
				 name = anmeldeView.registrierenNametxt.getText();
				 pw = anmeldeView.registrierenPwtxt.getText();
				 ip = anmeldeView.registrierenIPtxt.getText();
				 String art = "registrieren";
				 
				 //Wenn der Benutzername zu kurz ist, erscheint die Meldung, dass der Name zu kurz ist
				 if(name.length()<5){
					 Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,anmeldeView.NameZuKurz);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true); 
				 }else{
				 
				 clientModel.Verbinden(ip, name, pw, art);
				 
				 try {
						Thread.sleep(300);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				 
				 String meldung = clientModel.getMeldung();
				 
				 //Wenn der Benutzername bereits vergeben ist, erscheint die Meldung, dass der Benutzername vergeben ist
				 if(meldung.equals("BenutzernameVergeben")){
					 Stage stage = new Stage();
						InfofensterView iview = new InfofensterView(stage,anmeldeView.BenutzernameVergeben);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
						stage.setAlwaysOnTop(true);
				 }
				 //Wenn der Benutzername nicht vergeben ist und das Passwort und der Name genug lang sind, erscheint die Registriermeldung
				 if(meldung.equals("RegistrierMeldung")){
					 Stage stage = new Stage();
						InfofensterView iview = new InfofensterView(stage,anmeldeView.RegistrierMeldung);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
						stage.setAlwaysOnTop(true);
						anmeldeView.stop();
						clientView.menuDateiAbmelden.setDisable(false);
						clientView.menuDateiLöschen.setDisable(false);
				 }
				 
				 //Wenn das Passwort zu kurz ist, erscheint die Meldung, dass das Passwort zu kurz ist
				 if(meldung.equals("PasswortZuKurz")){
					 Stage stage = new Stage();
						InfofensterView iview = new InfofensterView(stage,anmeldeView.PasswortZuKurz);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
						stage.setAlwaysOnTop(true);
				 }
				 }
			}
		});
		
		
		clientView.menuHilfeRegeln.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e2){
				Label label;
				//Fenster mit Regeln
				Stage stage = new Stage();
				InfofensterView iview = new InfofensterView(stage,anmeldeView.regeln);
				InfofensterController icontroller = new InfofensterController(iview);
				iview.start();
				stage.setAlwaysOnTop(true);
			}
		});
		
		anmeldeView.menuHilfeRegeln.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				InfofensterView iview = new InfofensterView(stage,anmeldeView.regeln);
				InfofensterController icontroller = new InfofensterController(iview);
				iview.start();	
				stage.setAlwaysOnTop(true);
			}
			
		});
		
	}
	
	
}
