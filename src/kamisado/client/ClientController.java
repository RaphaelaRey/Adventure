package kamisado.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kamisado.ServiceLocator;
import kamisado.client.anmeldefenster.AnmeldefensterController;
import kamisado.client.anmeldefenster.AnmeldefensterView;
import kamisado.client.infofenster.InfofensterController;
import kamisado.client.infofenster.InfofensterView;
import kamisado.client.löschenfenster.LöschenfensterController;
import kamisado.client.löschenfenster.LöschenfensterView;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Translator;
import kamisado.commonClasses.Turm;

//TODO Team Wie wird definiert, wer die schwarzen Türme hat? Derjenige, der am wenigsten oft gespielt hat? Und wenn gleich dann zufällig?

public class ClientController {
	 	
	final private ClientModel clientModel;
	final private ClientView view;
	final private Spielbrett spielbrett;	
	private AnmeldefensterController anmeldeController;
	
	// Konstruktor
	public ClientController(ClientModel clientModel, ClientView view, AnmeldefensterController anmeldeController) {
		this.clientModel = clientModel;
		this.anmeldeController=anmeldeController;
		this.view = view;
		spielbrett = view.spielbrett; 
				
		// Schwarze Türme für Spielbeginn aktivieren
		for (int i = 0; i < Spielbrett.getTürme().length; i++){				
			Turm t = Spielbrett.getTürme()[i];				
			t.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event){
					if(spielbrett.istTurmBewegt()==false && t.getStroke()==Color.BLACK){	
						clientModel.turmStrokeWidthZurücksetzen();
						t.setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
						spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());
						clientModel.möglicheFelderAnzeigen(spielbrett.getAktiverTurmKoordinaten());
						
					}				
  				}
			});
		}
		
		// Turm bewegen, überprüfen ob jemand die Grundlinie des Gegners erreicht hat, ob jemand gewonnen hat und das Spiel zurücksetzen			
		for(int i = 0; i < spielbrett.getFelder().length; i++){
			for (int j = 0; j < spielbrett.getFelder().length; j++){
				Feld ausgewähltesFeld = spielbrett.getFelder()[i][j];
				ausgewähltesFeld.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event){
						Label label;
						if (spielbrett.getMöglicheFelder().contains(ausgewähltesFeld.getKoordinaten())){
							int[] nächsterAktiverTurm = new int[2];
							for (int k = 0; k < Spielbrett.getTürme().length; k++){
								if(clientModel.koordVergleich(Spielbrett.getTürme()[k].getKoordinaten(), spielbrett.getAktiverTurmKoordinaten())){ //aktiver Turm herausfinden
									clientModel.turmBewegen(ausgewähltesFeld, k);
									// Überprüfen, ob es einen Gewinner gibt 
									spielbrett.setGewinner(clientModel.gewinnerDefinieren(ausgewähltesFeld));
									// Zukünftiger gegnerischer Turm definieren und mögliche Felder anzeigen (sofern das Spiel nicht schon beendet ist)
									if(spielbrett.getGewinner()==null){
										nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurm(k, ausgewähltesFeld, nächsterAktiverTurm);							
									}
								}	 
							}
							// Zukünftiger gegnerischer Turm definieren im Fall einer Blockade 			
							if(spielbrett.getGewinner()==null && Spielbrett.isBlockiert()==true){
								nächsterAktiverTurm=clientModel.setNächsterGegnerischerTurmBlockade(nächsterAktiverTurm);	 
								if(Spielbrett.getBlockadenVerursacher()==Color.BLACK){
									//Blockadenmeldung
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.BlockadeMeldungSchwarz);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								} else if (Spielbrett.getBlockadenVerursacher()==Color.WHITE){
									//Blockadenmeldung
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.BlockadeMeldungWeiss);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								}
							} 			
							// Völliger Stillstand TODO Raphaela kontrollieren
							if(Spielbrett.getBlockadenCounter()==2){
								if(Spielbrett.getBlockadenVerursacher()==Color.BLACK){
									spielbrett.setGewinner(Color.WHITE);
									System.out.println("Schwarz hat totalen Stillstand verursacht, weiss gewinnt"); 
									// Gewinnermeldung bei völligem Stillstand
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungStillstandWeiss);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								} else{
									spielbrett.setGewinner(Color.BLACK);
									System.out.println("Weiss hat totalen Stillstand verursacht, schwarz gewinnt");
									//Gewinnermeldung bei völligem Stillstand
									Stage stage = new Stage();
									InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungStillstandSchwarz);
									InfofensterController icontroller = new InfofensterController(iview);
									iview.start();
								}
							}
							 
							// Überprüfen, wer gewonnen hat und die entsprechende Meldung anzeigen
							if(spielbrett.getGewinner() == Color.BLACK){
								//Gewinnermeldung inkl. Frage ob nochmals gespielt werden will (im Moment wird nur das Spielbrett zurückgesetzt)
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungSchwarz);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
							} else if(spielbrett.getGewinner() == Color.WHITE){
								//Gewinnermeldung 
								Stage stage = new Stage();
								InfofensterView iview = new InfofensterView(stage,view.GewinnerMeldungWeiss);
								InfofensterController icontroller = new InfofensterController(iview);
								iview.start();
							}
							
							// Spiel zurücksetzen nach Gewinn 
							if(spielbrett.getGewinner()!=null){
								clientModel.spielZurücksetzen(spielbrett.getMöglicheFelder(), spielbrett.getFelder(), Spielbrett.getTürme());	
							}
							// Koordinaten des nächsten aktiven Turms ausserhalb der for-Schleife definieren
							spielbrett.setAktiverTurmKoordinaten(nächsterAktiverTurm);	
							
						}
						clientModel.TürmeSenden();
						clientModel.KoordinatenSenden();
						clientModel.MFelderSenden();
					}					
				});				
			}
		}
		
		//Wenn sich der Benutzer abmeldet, erscheint wieder das Anmeldefenster
		view.menuDateiAbmelden.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				AnmeldefensterView anmeldeView = new AnmeldefensterView(stage,anmeldeController, view);
				AnmeldefensterController anmeldeController = new AnmeldefensterController(anmeldeView, view, clientModel);
				clientModel.clientAnhalten();
				anmeldeView.start();
				stage.setAlwaysOnTop(true);
				
			}
			
		});
		
		//Wenn der Benutzer auf löschen klickt, popt ein neues Fenster für die Eingaben zur Löschung eines Accoutns auf
		view.menuDateiLöschen.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				LöschenfensterView lview = new LöschenfensterView(stage);
				LöschenfensterController lcontroller = new LöschenfensterController(lview, view);
				lview.start();
				stage.setAlwaysOnTop(true);
			}
			
		});
	}
	
	
}