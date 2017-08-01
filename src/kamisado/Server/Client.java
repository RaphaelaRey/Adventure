package kamisado.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {
	
	protected Socket client;
    protected PrintWriter out;
    private final Logger logger = Logger.getLogger("");

    public Client(Socket client) {
        this.client = client;
        
        
        
    }
}
