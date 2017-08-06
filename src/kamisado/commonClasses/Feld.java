package kamisado.commonClasses;

import java.io.Serializable;

import javafx.scene.shape.Rectangle;

/**
 * @author Raphaela Rey
 */

public class Feld extends Rectangle implements Serializable{
	
	private boolean feldBesetzt = false;
	int[] koordinaten = new int[2];
	
	// Konstruktor: Die Grösse und die Koordinaten des Felds (x- und y-Koordinate in int-Array) werden mitgegeben
	public Feld(int grösse, int[] koord) {
		super(grösse, grösse);
		setKoordinaten(koord);
	}

	// Getter und setter für die Feldkoordinaten
	public int[] getKoordinaten() {
		return koordinaten;
	}
	public void setKoordinaten(int[] koordinaten) {
		this.koordinaten = koordinaten;
	}
	
	// Getter und setter um herauszufinden, ob ein Feld besetzt ist und um ein Feld zu besetzen
		public boolean istFeldBesetzt() {
			return feldBesetzt;
		}
		public void setFeldBesetzt(boolean feldBesetzt) {
			this.feldBesetzt = feldBesetzt;
		}

}
