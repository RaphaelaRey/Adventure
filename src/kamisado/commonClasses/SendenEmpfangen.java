package kamisado.commonClasses;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import kamisado.Server.ServerModel;
import kamisado.client.ClientModel;
import kamisado.client.ClientView;

/**
 * @author Tobias Deprato
 */

public class SendenEmpfangen {
	
	private static final Logger logger = Logger.getLogger("");
	private static Turm[] türme;
	
	public static void Senden(Socket clientSocket, Turm[] Türme){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(Türme);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static Turm[] Empfangen(Socket clientSocket){
		ObjectInputStream empfangen;
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			Turm[] in = (Turm[]) empfangen.readObject();
			logger.info("Neue Türme erhalten");
			
			if(türme != in){
				türme = in;
				logger.info("Koordinaten ersetzt");
				}
				//else do nothing
			
		} catch (Exception e){
			logger.info(e.toString());
		}
		return türme;
	}
	
	public static void setTürme(Turm[] Türme){
		türme = Türme;
	}
	

}
