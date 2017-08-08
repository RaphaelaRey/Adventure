package kamisado.commonClasses;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.scene.input.MouseEvent;

/**
 * @author Tobias Deprato
 */

public class SendenEmpfangen {
	
	private static final Logger logger = Logger.getLogger("");
	private static Turm[] neueTürme;
	private static String namePW;
	private static int[] koordinaten;
	private static ArrayList<int[]> möglicheFelder;
	
	public static void Senden(Socket clientSocket, Turm[] Türme){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(Türme);
			senden.flush();
			logger.info("Neue Türme gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Senden(Socket clientSocket, String namePW){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(namePW);
			senden.flush();
			logger.info("Anmeldedaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Senden(Socket clientSocket, int[] Koord){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(Koord);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	public static void Senden(Socket clientSocket, ArrayList<int[]> mFelder){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(mFelder);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Senden(Socket clientSocket, boolean schwarz){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(schwarz);
			senden.flush();
			logger.info("Schwarz ist " + schwarz + " gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static Turm[] Empfangen(Socket clientSocket){
		ObjectInputStream empfangen;
		Turm[] in;
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			 in = (Turm[]) empfangen.readObject();
			logger.info("Neue Türme erhalten");
			setTürme(in);			
		} catch (Exception e){
			logger.info(e.toString());
		}
		return neueTürme;
	}	
	
	public static ArrayList<int[]> EmpfangenMF(Socket clientSocket){
		ObjectInputStream empfangen;
		ArrayList<int[]> mFelder;
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			 mFelder = (ArrayList<int[]>) empfangen.readObject();
			logger.info("Neue Türme erhalten");
			setMöglicheFelder(möglicheFelder);			
		} catch (Exception e){
			logger.info(e.toString());
		}
		return möglicheFelder;
	}	
	
	public static int[] EmpfangenInt(Socket clientSocket){
		ObjectInputStream empfangen;
		int[] in;
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			 in = (int[]) empfangen.readObject();
			logger.info("Neue Türme erhalten");
			setKoordinaten(in);			
		} catch (Exception e){
			logger.info(e.toString());
		}
		return koordinaten;
	}	
	
	public static String EmpfangenString(Socket clientSocket){
		ObjectInputStream empfangen;
		String in; 
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			 in = (String) empfangen.readObject();
			logger.info("Name und PW erhalten");
			setString(in);		
		} catch (Exception e){
			logger.info(e.toString());
		}
		return namePW;
	}	
	public static void setTürme(Turm[] Türme){
		neueTürme = Türme;
	}
	
	public static void setString(String s){
		namePW = s;
	}

	public static int[] getKoordinaten() {
		return koordinaten;
	}

	public static void setKoordinaten(int[] koordinaten) {
		SendenEmpfangen.koordinaten = koordinaten;
	}

	public static ArrayList getMöglicheFelder() {
		return möglicheFelder;
	}

	public static void setMöglicheFelder(ArrayList möglicheFelder) {
		SendenEmpfangen.möglicheFelder = möglicheFelder;
	}
	

}
