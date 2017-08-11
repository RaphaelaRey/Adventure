package kamisado.commonClasses;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Tobias Deprato
 */

public class SendenEmpfangen {
	
	private static final Logger logger = Logger.getLogger("");
	private static int[] koordinaten;
	
	/** sendet ein TurmArray
	 * @param Turm[]
	 * @author Tobias Deprato 
	 */	
	public static void Senden(Socket clientSocket, Turm[] Türme){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());

			
			//Turm[] an Client senden
			senden.writeObject(Türme);
			senden.flush();

		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	/** sendet ein String
	 * @param String
	 * @author Tobias Deprato 
	 */	
	public static void Senden(Socket clientSocket, String string){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			
			//String an Client senden
			senden.writeObject(string);
			senden.flush();
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
}
