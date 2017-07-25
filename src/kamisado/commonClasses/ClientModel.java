package kamisado.commonClasses;

import java.io.IOException;
import java.net.Socket;

import kamisado.Server.ServerModel;

public class ClientModel {
	private Socket socket;
	private String name;
	private ServerModel model;

	public ClientModel(ServerModel model, Socket socket) {
		this.model = model;
		this.socket = socket;

		//Thread erstellen um mehrere Clients zu starten
		Runnable r = new Runnable() {
			@Override
			public void run() {
				//while Schleife mit Object in- und out-putstream

			}
		};
		Thread t = new Thread(r);
		t.start();
	}

}
