package kamisado.Server;

import java.net.Socket;
import java.util.logging.Logger;

import kamisado.commonClasses.SendenEmpfangen;

public class Client {
	
	private Socket ClientSocket;
	private String name;
	private ServerModel model;
	private final Logger logger = Logger.getLogger("");

	protected Client(ServerModel model, Socket socket) {
		this.model = model;
		this.ClientSocket = socket;

		// Create thread to read incoming messages
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while(true) {
					SendenEmpfangen.Empfangen(ClientSocket);
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
        
}

