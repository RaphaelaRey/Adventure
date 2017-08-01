package kamisado.commonClasses;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class SendenEmpfangen {
	
	private static Turm[] türme;
	private static final Logger logger = Logger.getLogger("");
	
	static ObjectOutputStream senden;
	static ObjectInputStream empfangen;
	
	public static void Senden(Socket clientSocket){
		
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");

			//neueKoordinaten an Client senden
			türme = kamisado.commonClasses.Spielbrett.getTürme();
			senden.writeObject(türme);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Empfangen(Socket clientSocket){
		
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("Streams erstellt");
		
			//neueKoordinaten von Client empfangen
			Turm[] in = (Turm[]) empfangen.readObject();
			türme= in;
			logger.info("Neue Koordinaten erhalten");
			
			if(türme != in){
				kamisado.commonClasses.Spielbrett.setTürme(türme);
				logger.info("Koordinaten ersetzt");
				}
				//else do nothing
			
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	

}
