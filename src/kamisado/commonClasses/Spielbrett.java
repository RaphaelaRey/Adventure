package kamisado.commonClasses;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import kamisado.client.ClientModel;

/**
 * @author Raphaela Rey
 */

public class Spielbrett{
	
	private ClientModel clientModel;
		
	private static boolean turmBewegt = false; // wenn diese Variable true ist, wurde bereits ein Turm bewegt -> nicht mehr der erste Spielzug
	private static int [] aktiverTurmKoordinaten = new int [2];
	
	// Infos des Spielbretts, der Türme und Felder als Konstanten definieren
	public final int SPIELBRETTHÖHE = 886;
	public final int SPIELBRETTBREITE = 826;
	public final int STROKEWIDTHTÜRMESTANDARD = 3;
	public final int STROKEWIDTHAUSGEWÄHLTERTURM = 8;
	public final int STROKEWIDTHFELDERSTANDARD = 1;
	public final int STROKEWIDTHMÖGLICHEFELDER = 5;
	public final int FELDGRÖSSE = 100;
	public final int TURMDURCHMESSER = 40;
	
	// Zweidimensionales Array für alle Felder erstellen
	private static Feld [][] felder = new Feld [8][8];	
	
	// Array mit Koordinaten der farbigen Felder erstellen
    private final static int [][] ORANGEFELDER = {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}};
    private final static int [][] BRAUNEFELDER = {{0,7},{1,6},{2,5},{3,4},{4,3},{5,2},{6,1},{7,0}};
    private final static int [][] PINKEFELDER = {{0,3},{1,2},{2,1},{3,0},{4,7},{5,6},{6,5},{7,4}};
    private final static int [][] GELBEFELDER = {{0,4},{1,5},{2,6},{3,7},{4,0},{5,1},{6,2},{7,3}};
    private final static int [][] BLAUEFELDER = {{0,5},{1,0},{2,3},{3,6},{4,1},{5,4},{6,7},{7,2}};
    private final static int [][] VIOLETTEFELDER = {{0,6},{1,3},{2,0},{3,5},{4,2},{5,7},{6,4},{7,1}};
    private final static int [][] ROTEFELDER = {{0,1},{1,4},{2,7},{3,2},{4,5},{5,0},{6,3},{7,6}};
    private final static int [][] GRÜNEFELDER = {{0,2},{1,7},{2,4},{3,1},{4,6},{5,3},{6,0},{7,5}};
    
    // Mögliche Gewinnerfelder
    public final static int [][] GEWINNERFELDERSCHWARZ = {{0,0},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0}};
    public final static int [][] GEWINNERFELDERWEISS = {{0,7},{1,7},{2,7},{3,7},{4,7},{5,7},{6,7},{7,7}};
    
    // Je ein Array für die beiden Turmfarben und alle Türme erstellen 
    private Turm [] schwarzeTürme = new Turm [8];					
    private Turm [] weisseTürme = new Turm [8];						
    private static Turm [] türme = new Turm [16];
    
    // Array für die Koordinaten der möglichen Felder erstellen
	private static ArrayList<int[]> möglicheFelder = new ArrayList<int[]>();
    
	// Gridpane für Spielbrett erstellen
	private GridPane gridpane = new GridPane(); 
    
	// Konstruktor: Felder und Türme erstellen, einfärben und der Gridpane hinzufügen
    public Spielbrett(ClientModel clientModel) {
    	this.clientModel = clientModel;
    	
		// Felder in einem zweidimensionalen Array erstellen und der Gridpane hinzufügen
    	for (int i = 0; i < felder.length; i++){
    		for (int j = 0; j < felder.length; j++){
    			felder[i][j] = new Feld(FELDGRÖSSE, new int[]{i, j});
    			gridpane.add(felder[i][j], i, j);
    			felder[i][j].setStroke(Color.BLACK);
    		}
    	}
		
    	// Felder einfärben
    	for (int i = 0; i < ORANGEFELDER.length; i++){
    		felder[ORANGEFELDER[i][0]][ORANGEFELDER[i][1]].setFill(Color.ORANGE);
    	}
    	for (int i = 0; i < BRAUNEFELDER.length; i++){
    		felder[BRAUNEFELDER[i][0]][BRAUNEFELDER[i][1]].setFill(Color.BROWN);
    	}
    	for (int i = 0; i < PINKEFELDER.length; i++){
    		felder[PINKEFELDER[i][0]][PINKEFELDER[i][1]].setFill(Color.HOTPINK);
    	}
    	for (int i = 0; i < GELBEFELDER.length; i++){
    		felder[GELBEFELDER[i][0]][GELBEFELDER[i][1]].setFill(Color.YELLOW);
    	}
    	for (int i = 0; i < BLAUEFELDER.length; i++){
    		felder[BLAUEFELDER[i][0]][BLAUEFELDER[i][1]].setFill(Color.MEDIUMBLUE);
    	}
    	for (int i = 0; i < VIOLETTEFELDER.length; i++){
    		felder[VIOLETTEFELDER[i][0]][VIOLETTEFELDER[i][1]].setFill(Color.DARKVIOLET);
    	}
    	for (int i = 0; i < ROTEFELDER.length; i++){
    		felder[ROTEFELDER[i][0]][ROTEFELDER[i][1]].setFill(Color.RED);
    	}
    	for (int i = 0; i < GRÜNEFELDER.length; i++){
    		felder[GRÜNEFELDER[i][0]][GRÜNEFELDER[i][1]].setFill(Color.GREEN);
    	}
		
    	// Schwarze Türme in Array erstellen, der Gridpane hinzufügen und einfärben gemäss den unterliegenden Feldern
    	for (int i = 0; i < schwarzeTürme.length; i++){
    		schwarzeTürme[i] = new Turm(TURMDURCHMESSER, new int[] {i, 7}); 
			gridpane.add(schwarzeTürme[i], i, 7);
			schwarzeTürme[i].setStroke(Color.BLACK);
			schwarzeTürme[i].setStrokeWidth(STROKEWIDTHTÜRMESTANDARD);
			schwarzeTürme[i].setFill(felder[i][7].getFill());
			schwarzeTürme[i].setFüllFarbe(felder[i][7].getFill().toString());
			schwarzeTürme[i].setStrokeFarbe((Color.BLACK).toString());
			schwarzeTürme[i].setTurmRadius(TURMDURCHMESSER);
			felder[i][7].setFeldBesetzt(true);
			
    	}
    	// Weisse Türme in Array erstellen, der Gridpane hinzufügen und einfärben gemäss den unterliegenden Feldern
    	for (int i = 0; i < weisseTürme.length; i++){
    		weisseTürme[i] = new Turm(TURMDURCHMESSER, new int[] {i, 0});
			gridpane.add(weisseTürme[i], i, 0);
			weisseTürme[i].setStroke(Color.WHITE);
			weisseTürme[i].setStrokeWidth(STROKEWIDTHTÜRMESTANDARD);
			weisseTürme[i].setFill(felder[i][0].getFill());
			weisseTürme[i].setFüllFarbe(felder[i][0].getFill().toString());
			weisseTürme[i].setStrokeFarbe((Color.WHITE).toString());
			weisseTürme[i].setTurmRadius(TURMDURCHMESSER);
			felder[i][0].setFeldBesetzt(true);
    	}
    	// Weisse und schwarze Türme in gemeinsames Array kopieren
    	System.arraycopy(schwarzeTürme, 0, getTürme(), 0, 8);
    	System.arraycopy(weisseTürme, 0, getTürme(), 8, 8);
 
    	// Elemente der GridPane zentrieren  
    	gridpane.setAlignment(Pos.CENTER);
    	ColumnConstraints[] constraints = new ColumnConstraints[schwarzeTürme.length];
    	for(int i = 0; i < schwarzeTürme.length; i++){
    		constraints[i] = new ColumnConstraints();
    		constraints[i].setHalignment(HPos.CENTER);
    	}
    	gridpane.getColumnConstraints().addAll(constraints);
    	
    }
 	
 	// Getter und setter
 	
 	
	

	public static boolean istTurmBewegt() {
		return turmBewegt;
	}

	public static void setTurmBewegt(boolean turmBewegt) {
		Spielbrett.turmBewegt = turmBewegt;
	}

	public static Turm [] getTürme() {
		return türme;
	}
	public static void setTürme(Turm [] Türme) {
		türme = Türme;
	}

	public Turm[] getSchwarzeTürme() {
		return schwarzeTürme;
	}
	public void setSchwarzeTürme(Turm[] schwarzeTürme) {
		this.schwarzeTürme = schwarzeTürme;
	}
	
	public Turm[] getWeisseTürme() {
		return weisseTürme;
	}
	public void setWeisseTürme(Turm[] weisseTürme) {
		this.weisseTürme = weisseTürme;
	}
	
	public GridPane getPane() {
		return gridpane;
	}
	public void setPane(GridPane pane) {
		this.gridpane = pane;
	}

	public static Feld[][] getFelder() {
		return felder;
	}
	public void setFelder(Feld[][] felder) {
		this.felder = felder;
	}

	public static ArrayList<int[]> getMöglicheFelder() {
		return möglicheFelder;
	}
	public static void setMöglicheFelder(ArrayList<int[]> möglicheFelder) {
		Spielbrett.möglicheFelder = möglicheFelder;
	}

	public static int [] getAktiverTurmKoordinaten() {
		return aktiverTurmKoordinaten;
	}

	public static void setAktiverTurmKoordinaten(int [] aktiverTurmKoordinaten) {
		Spielbrett.aktiverTurmKoordinaten = aktiverTurmKoordinaten;
	}
}