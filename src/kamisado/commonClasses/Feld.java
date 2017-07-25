package kamisado.commonClasses;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Feld extends Rectangle{
	
	private boolean feldBesetzt = false;
	int[] koordinaten = new int[2];
	
	// Feld-Konstruktor: Die Grösse und die Koordinaten des Felds (x- und y-Koordinate in Array) werden mitgegeben
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
	
	// Getter und setter für um herauszufinden, ob ein Feld besetzt ist und ein Feld zu besetzen
		public boolean istFeldBesetzt() {
			return feldBesetzt;
		}
		public void setFeldBesetzt(boolean feldBesetzt) {
			this.feldBesetzt = feldBesetzt;
		}
	
	// getter für die Feldfarbe
	public Color getFeldFarbe()	{
		return (Color) super.getFill();
	}
	
}
