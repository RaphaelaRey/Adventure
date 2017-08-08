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
import kamisado.client.anmeldefenster.AnmeldefensterView;
import kamisado.client.infofenster.InfofensterController;
import kamisado.client.infofenster.InfofensterView;
import kamisado.commonClasses.Translator;

public class LöschenfensterController {
	
	final private LöschenfensterView löschenView;
	private AnmeldefensterView anmeldeView;

	public LöschenfensterController(LöschenfensterView löschenView, AnmeldefensterView anmeldeView) {
		this.löschenView = löschenView;
		this.anmeldeView = anmeldeView;
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Translator t = sl.getTranslator();
		
		löschenView.löschenBestätigen.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				String name = löschenView.löschenNametxt.getText();
				String pw = löschenView.löschenPWtxt.getText();
				
				try {
					FileReader fr = new FileReader("src/kamisado/registrierungen.txt");
					BufferedReader reader = new BufferedReader(fr);
					FileReader fr2 = new FileReader("src/kamisado/registrierungen.txt");
					BufferedReader reader2 = new BufferedReader(fr2);
					FileReader fr3 = new FileReader("src/kamisado/registrierungen.txt");
					BufferedReader reader3 = new BufferedReader(fr3);
					
					String zeile;
					Label label;
					boolean benutzerExistiert = false;
					while ((zeile = reader.readLine())!=null){
						String[] parts = zeile.split(",");
						if(parts[0].equals(name)&&parts[1].equals(pw)){
							benutzerExistiert = true;
							String neueZeile;
							int counter=0;
							while((neueZeile = reader2.readLine())!=null){
								counter++;
							}
							
							String[] alle = new String[counter];
							String vergleich = name+","+pw;
							for(int i = 0; i<counter;i++){
								while ((neueZeile = reader3.readLine())!= null){
									if(neueZeile.equals(vergleich)){
										alle[i]="";
									}else{							
										alle[i]=neueZeile;
										
										}break;
								}	
							}
							FileWriter fw = new FileWriter("src/kamisado/registrierungen.txt");
							BufferedWriter löschen = new BufferedWriter(fw);
							löschen.write("");
							löschen.close();
							fw.close();
							
							for(int i=0; i<alle.length;i++){
								FileWriter schreiben = new FileWriter("src/kamisado/registrierungen.txt",true);
								String s = alle[i];
								schreiben.write(s);
								schreiben.write("\n");
								schreiben.close();
							}	
							//TODO Carmen neues Infofenster mit Meldung, dass löschen erfolgreich war
							Stage stage = new Stage();
							InfofensterView iview = new InfofensterView(stage,anmeldeView.ErfolgreichMeldung);
							InfofensterController icontroller = new InfofensterController(iview);
							iview.start();
							stage.setAlwaysOnTop(true);
							break;
						}else if(parts[0].equals(name)&&!parts[1].equals(pw)){
							benutzerExistiert = true;
							//TODO Carmen neues Infofenster mit Meldung, dass PW falsch ist
							Stage stage = new Stage();
							InfofensterView iview = new InfofensterView(stage,anmeldeView.PasswortFalschMeldung);
							InfofensterController icontroller = new InfofensterController(iview);
							iview.start();
							stage.setAlwaysOnTop(true);
						}
					}
					if(benutzerExistiert==false){
						//TODO Carmen Meldung, dass Benutzer nicht existiert
						Stage stage = new Stage();
						InfofensterView iview = new InfofensterView(stage,anmeldeView.BenutzerExistiertNichtMeldung);
						InfofensterController icontroller = new InfofensterController(iview);
						iview.start();
						stage.setAlwaysOnTop(true);
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
			
		});
		
	}

}
