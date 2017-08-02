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

public class Spielbrett {
	
	protected ClientModel clientModel;
		
	private boolean turmBewegt = false; // wenn diese Variable true ist, wurde bereits ein Turm bewegt -> nicht mehr der erste Spielzug
	private boolean spielBeendet = false; // wenn diese Variable true ist, wurde ein Spiel beendet	
	protected static int [] aktiverTurmKoordinaten = new int [2];

	// Randbreite der Türme und Felder als Konstanten definieren
	public final int STROKEWIDTHTÜRMESTANDARD = 3;
	public final int STROKEWIDTHAUSGEWÄHLTERTURM = 8;
	public final int STROKEWIDTHMÖGLICHEFELDER = 5;
	public final int FELDGRÖSSE = 100;
	public final int TURMDURCHMESSER = 40;
	
	// Zweidimensionales Array für alle Felder erstellen
	private Feld [][] felder = new Feld [8][8];	
	
	// Array mit Koordinaten der farbigen Felder erstellen
    private final int [][] orangeFelder = {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}};
    private final int [][] brauneFelder = {{0,7},{1,6},{2,5},{3,4},{4,3},{5,2},{6,1},{7,0}};
    private final int [][] pinkeFelder = {{0,3},{1,2},{2,1},{3,0},{4,7},{5,6},{6,5},{7,4}};
    private final int [][] gelbeFelder = {{0,4},{1,5},{2,6},{3,7},{4,0},{5,1},{6,2},{7,3}};
    private final int [][] blaueFelder = {{0,5},{1,0},{2,3},{3,6},{4,1},{5,4},{6,7},{7,2}};
    private final int [][] violetteFelder = {{0,6},{1,3},{2,0},{3,5},{4,2},{5,7},{6,4},{7,1}};
    private final int [][] roteFelder = {{0,1},{1,4},{2,7},{3,2},{4,5},{5,0},{6,3},{7,6}};
    private final int [][] grüneFelder = {{0,2},{1,7},{2,4},{3,1},{4,6},{5,3},{6,0},{7,5}};
    
    // Mögliche Gewinnerfelder
    protected final int [][] gewinnerFelderSchwarz = {{0,0},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0}};
    protected final int [][] gewinnerFelderWeiss = {{0,7},{1,7},{2,7},{3,7},{4,7},{5,7},{6,7},{7,7}};

    
    // Je ein Array für die beiden Turmfarben und alle Türme erstellen 
    private Turm [] schwarzeTürme = new Turm [8];					
    private Turm [] weisseTürme = new Turm [8];						
    private static Turm [] türme = new Turm [16];
    
    // Array für die Koordinaten der möglichen Felder erstellen
	protected ArrayList<int[]> möglicheFelder = new ArrayList<int[]>();
    
	private GridPane pane = new GridPane();
    
	// Konstruktor: Felder und Türme erstellen, einfärben und der Gridpane hinzufügen
    public Spielbrett(ClientModel clientModel) {
    	this.clientModel = clientModel;
    	
		// Felder in einem zweidimensionalen Array erstellen und der Gridpane hinzufügen
    	for (int i = 0; i < felder.length; i++){
    		for (int j = 0; j < felder.length; j++){
    			felder[i][j] = new Feld(FELDGRÖSSE, new int[]{i, j});
    			pane.add(felder[i][j], i, j);
    			felder[i][j].setStroke(Color.BLACK);
    		}
    	}
		
    	// Felder einfärben
    	for (int i = 0; i < orangeFelder.length; i++){
    		felder[orangeFelder[i][0]][orangeFelder[i][1]].setFill(Color.ORANGE);
    	}
    	for (int i = 0; i < brauneFelder.length; i++){
    		felder[brauneFelder[i][0]][brauneFelder[i][1]].setFill(Color.BROWN);
    	}
    	for (int i = 0; i < pinkeFelder.length; i++){
    		felder[pinkeFelder[i][0]][pinkeFelder[i][1]].setFill(Color.HOTPINK);
    	}
    	for (int i = 0; i < gelbeFelder.length; i++){
    		felder[gelbeFelder[i][0]][gelbeFelder[i][1]].setFill(Color.YELLOW);
    	}
    	for (int i = 0; i < blaueFelder.length; i++){
    		felder[blaueFelder[i][0]][blaueFelder[i][1]].setFill(Color.MEDIUMBLUE);
    	}
    	for (int i = 0; i < violetteFelder.length; i++){
    		felder[violetteFelder[i][0]][violetteFelder[i][1]].setFill(Color.DARKVIOLET);
    	}
    	for (int i = 0; i < roteFelder.length; i++){
    		felder[roteFelder[i][0]][roteFelder[i][1]].setFill(Color.RED);
    	}
    	for (int i = 0; i < grüneFelder.length; i++){
    		felder[grüneFelder[i][0]][grüneFelder[i][1]].setFill(Color.GREEN);
    	}
		
    	// Schwarze Türme in Array erstellen, der Gridpane hinzufügen und einfärben gemäss den unterliegenden Feldern
    	for (int i = 0; i < schwarzeTürme.length; i++){
    		schwarzeTürme[i] = new Turm(TURMDURCHMESSER, new int[] {i, 7}); 
			pane.add(schwarzeTürme[i], i, 7);
			schwarzeTürme[i].setStroke(Color.BLACK);
			schwarzeTürme[i].setStrokeWidth(STROKEWIDTHTÜRMESTANDARD);
			schwarzeTürme[i].setFill(felder[i][7].getFill());
			felder[i][7].setFeldBesetzt(true);
			
    	}
    	// Weisse Türme in Array erstellen, der Gridpane hinzufügen und einfärben gemäss den unterliegenden Feldern
    	for (int i = 0; i < weisseTürme.length; i++){
    		weisseTürme[i] = new Turm(TURMDURCHMESSER, new int[] {i, 0});
			pane.add(weisseTürme[i], i, 0);
			weisseTürme[i].setStroke(Color.WHITE);
			weisseTürme[i].setStrokeWidth(STROKEWIDTHTÜRMESTANDARD);
			weisseTürme[i].setFill(felder[i][0].getFill());
			felder[i][0].setFeldBesetzt(true);
    	}
    	// Weisse und schwarze Türme in gemeinsames Array kopieren
    	System.arraycopy(schwarzeTürme, 0, getTürme(), 0, 8);
    	System.arraycopy(weisseTürme, 0, getTürme(), 8, 8);
 
    	// Elemente der GridPane zentrieren  
    	pane.setAlignment(Pos.CENTER);
    	ColumnConstraints[] constraints = new ColumnConstraints[schwarzeTürme.length];
    	for(int i = 0; i < schwarzeTürme.length; i++){
    		constraints[i] = new ColumnConstraints();
    		constraints[i].setHalignment(HPos.CENTER);
    	}
    	pane.getColumnConstraints().addAll(constraints);
    	
    }
 	
 	// Getter und setter
 	public int[] getAktiverTurmKoordinaten() {
 		return aktiverTurmKoordinaten;
 	}
 	public void setAktiverTurmKoordinaten(int[] aktiverTurmKoordinaten) {
 		this.aktiverTurmKoordinaten = aktiverTurmKoordinaten;
 	}
 	
	public boolean istTurmBewegt() {
		return turmBewegt;
	}
	public void setTurmBewegt(boolean turmBewegt) {
		this.turmBewegt = turmBewegt;
	}

	public static Turm [] getTürme() {
		return türme;
	}
	public static void setTürme(Turm [] neuTürme) {
		türme = neuTürme;
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
		return pane;
	}
	public void setPane(GridPane pane) {
		this.pane = pane;
	}

	public Feld[][] getFelder() {
		return felder;
	}
	public void setFelder(Feld[][] felder) {
		this.felder = felder;
	}

	public boolean istSpielBeendet() {
		return spielBeendet;
	}
	public void setSpielBeendet(boolean spielGewonnen) {
		this.spielBeendet = spielGewonnen;
	}

	public ArrayList<int[]> getMöglicheFelder() {
		return möglicheFelder;
	}
	public void setMöglicheFelder(ArrayList<int[]> möglicheFelder) {
		this.möglicheFelder = möglicheFelder;
	}

	public int[][] getGewinnerFelderSchwarz() {
		return gewinnerFelderSchwarz;
	}

	public int[][] getGewinnerFelderWeiss() {
		return gewinnerFelderWeiss;
	}
}