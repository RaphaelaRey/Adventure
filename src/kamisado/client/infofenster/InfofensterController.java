package kamisado.client.infofenster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author carmen walser
 *
 */
public class InfofensterController {
	
	private InfofensterView infoView;
	

	public InfofensterController(InfofensterView infoView) {
		this.infoView=infoView;
		
		infoView.ok.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				infoView.stop();
				
			}
		});
	}

}
