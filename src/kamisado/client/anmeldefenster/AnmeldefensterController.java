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
import kamisado.client.ClientView;
import kamisado.client.infofenster.InfofensterController;
import kamisado.client.infofenster.InfofensterView;
import kamisado.commonClasses.Translator;

/**
 * @author carmen walser
 *
 */
public class AnmeldefensterController {
	
	private AnmeldefensterView anmeldeView;
	private ClientView clientView;
	
	private String name;
	private String pw;
	
	public AnmeldefensterController(AnmeldefensterView anmeldeView, ClientView clientView) {
		//TODO wo muss das Textfile gespeichert werden, damit beide Clients auf das selbe File zugreifen bei der Anmeldung
		this.anmeldeView = anmeldeView;
		this.clientView= clientView;
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		
		anmeldeView.btnAnmelden.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				name=anmeldeView.anmeldenNametxt.getText();
				pw=anmeldeView.anmeldenPwtxt.getText();
				
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
								label=new Label(t.getString("StartMeldung"));
								InfofensterView iview = new InfofensterView(stage,label);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
								anmeldeView.stop();
							}else if(parts[0].equals(name)&&!parts[1].equals(pw)){
								benutzerExistiert = true;
								//Meldung, dass Passwort falsch ist
								Stage stage = new Stage();
								label=new Label(t.getString("PWfalschMeldung"));
								InfofensterView iview = new InfofensterView(stage,label);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
							}													
					}
					if(benutzerExistiert==false){
						//Meldung, dass Benutzer nicht exisitert
						Stage stage = new Stage();
						label=new Label(t.getString("BenutzerExistiertNicht"));
						InfofensterView iview = new InfofensterView(stage,label);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
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
							label = new Label(t.getString("BenutzernameVergeben"));
							InfofensterView iview = new InfofensterView(stage, label);
							iview.start();
							benutzerVergeben = true;
						} 
					}
					//Wenn der Benutzername nicht vergeben ist und das Passwort genügend Zeichen beinhaltet, werden die Daten in das File geschrieben
					if(benutzerVergeben==false){
						if(pw.length()>=5){
							FileWriter fw = new FileWriter("registrierungen.txt", true);
							fw.write(name+",");
							fw.write(pw);
							fw.write("\n");
							fw.close();
							
							//Meldung, dass die Registrierung erfolgreich war
							Stage stage = new Stage();
							label=new Label(t.getString("RegistrierMeldung"));
							InfofensterView iview = new InfofensterView(stage,label);
							InfofensterController icontroller = new InfofensterController(iview);
							iview.start();
						}	else{
							//Meldung, dass Passwort zu kurz ist
							Stage stage = new Stage();
							label=new Label(t.getString("PasswortZuKurz"));
							InfofensterView iview = new InfofensterView(stage,label);
							InfofensterController icontroller = new InfofensterController(iview);
							iview.start();
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
				label=new Label(t.getString("Regeln"));
				InfofensterView iview = new InfofensterView(stage,label);
				InfofensterController icontroller = new InfofensterController(iview);
				iview.start();
				stage.setAlwaysOnTop(true);
			}
		});
		
		anmeldeView.menuHilfeRegeln.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
//				Label label;
				Stage stage = new Stage();
//				label=new Label(t.getString("Regeln"));
				InfofensterView iview = new InfofensterView(stage,anmeldeView.regeln);
				InfofensterController icontroller = new InfofensterController(iview);
				iview.start();	
				stage.setAlwaysOnTop(true);
			}
			
		});
		
		
		
	}
	
	public String getName(){
		return name;
	}
	
	public String getPasswort(){
		return pw;
	}

}
