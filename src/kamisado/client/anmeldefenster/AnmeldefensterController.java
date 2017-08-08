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
				
				try {
					FileReader fr = new FileReader("src/kamisado/registrierungen.txt");
					BufferedReader reader = new BufferedReader(fr);
					String zeile;
					Label label;
					boolean benutzerExistiert = false;
					while((zeile=reader.readLine())!=null){
						String[] parts = zeile.split(",");
							//Überprüfung der Bedingungen, dass Benutzer angemeldet ist
							if(parts[0].equals(name)&&parts[1].equals(pw)){
								benutzerExistiert = true;
								clientView.menuDateiAbmelden.setDisable(false);
								clientView.menuDateiLöschen.setDisable(false);
								//Meldung zu Beginn des Spiels
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,anmeldeView.startMeldung);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
								stage.setAlwaysOnTop(true);
								clientModel.Verbinden(ip, name, pw);
								anmeldeView.stop();
							}else if(parts[0].equals(name)&&!parts[1].equals(pw)){
								benutzerExistiert = true;
								//Meldung, dass Passwort falsch ist
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,anmeldeView.PWfalschMeldung);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
								stage.setAlwaysOnTop(true);
							}													
					}
					if(benutzerExistiert==false){
						//Meldung, dass Benutzer nicht exisitert
						Stage stage = new Stage();
						InfofensterView iview = new InfofensterView(stage,anmeldeView.BenutzerExistiertNicht);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
						stage.setAlwaysOnTop(true);
					}
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		anmeldeView.btnRegistrieren.setOnAction(new EventHandler<ActionEvent> (){
			public void handle(ActionEvent e2){
				 name = anmeldeView.registrierenNametxt.getText();
				 pw = anmeldeView.registrierenPwtxt.getText();	
				 			 				 
				 //überprüfen, ob Benutzername bereits vergeben ist
				 try {
					FileReader fr = new FileReader("registrierungen.txt");
					BufferedReader reader = new BufferedReader(fr);
					String zeile;
					Label label;
					boolean benutzerVergeben = false;
					while((zeile=reader.readLine())!= null){
						String[] parts = zeile.split(",");
							
						if(parts[0].equals(name)){
							//Meldung, dasss Benutzername bereits vergeben ist
							Stage stage = new Stage();
							InfofensterView iview = new InfofensterView(stage, anmeldeView.BenutzernameVergeben);
							iview.start();
							stage.setAlwaysOnTop(true);
							benutzerVergeben = true;
						} 
					}
					//Wenn der Benutzername nicht vergeben ist und das Passwort genügend Zeichen beinhaltet, werden die Daten in das File geschrieben
					if(benutzerVergeben==false){
						if(pw.length()>=5){
							FileWriter fw = new FileWriter("src/kamisado/registrierungen.txt", true);
							fw.write(name+",");
							fw.write(pw);
							fw.write("\n");
							fw.close();
							
							//Meldung, dass die Registrierung erfolgreich war
							Stage stage = new Stage();
							InfofensterView iview = new InfofensterView(stage,anmeldeView.RegistrierMeldung);
							InfofensterController icontroller = new InfofensterController(iview);
							iview.start();
							stage.setAlwaysOnTop(true);
						}	else{
							//Meldung, dass Passwort zu kurz ist
							Stage stage = new Stage();
							InfofensterView iview = new InfofensterView(stage,anmeldeView.PasswortZuKurz);
							InfofensterController icontroller = new InfofensterController(iview);
							iview.start();
							stage.setAlwaysOnTop(true);
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
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
		
		anmeldeView.menuDateiLöschen.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				LöschenfensterView lview = new LöschenfensterView(stage);
				LöschenfensterController lcontroller = new LöschenfensterController(lview, anmeldeView);
				lview.start();
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
