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
				
				String meldung = clientModel.getMeldung();
				
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
				if(meldung.equals("PasswortFalsch")){
					Stage stage = new Stage();
					InfofensterView iview = new InfofensterView(stage,anmeldeView.PWfalschMeldung);
					InfofensterController icontroller = new InfofensterController(iview);
					iview.start();
					stage.setAlwaysOnTop(true);
				}
				
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
				 
				 clientModel.Verbinden(ip, name, pw, art);
				 
				 String meldung = clientModel.getMeldung();
				 
				 if(meldung.equals("BenutzernameVergeben")){
					 Stage stage = new Stage();
						InfofensterView iview = new InfofensterView(stage,anmeldeView.BenutzernameVergeben);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
						stage.setAlwaysOnTop(true);
				 }
				 
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
				 
				 if(meldung.equals("PasswortZuKurz")){
					 Stage stage = new Stage();
						InfofensterView iview = new InfofensterView(stage,anmeldeView.PasswortZuKurz);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
						stage.setAlwaysOnTop(true);
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
	
	public static String getName(){
		return name;
	}
	
	public static String getPasswort(){
		return pw;
	}
	public static String getIP(){
		return ip;
	}

}
