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
	private static String namePW;
	private static int[] koordinaten;
	
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
	
	public static void Senden(Socket clientSocket, boolean tmpBol){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(tmpBol);
			senden.flush();
			logger.info("Anmeldedaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
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

}
