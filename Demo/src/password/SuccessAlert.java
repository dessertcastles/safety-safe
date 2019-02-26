package password;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class SuccessAlert {
	
	public static void success() {
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Success!");
		window.setMinWidth(350);
		window.setMinHeight(150);
		
		Label successMessage = new Label("Your password has been successfully saved.");
		
		Button ok = new Button("OK");
		ok.setOnAction(event -> {
			window.close();
		});
		
		VBox layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(successMessage, ok);
		
		Scene scene = new Scene(layout, 350, 150);
		
		window.setScene(scene);
		window.showAndWait();
		
	}
}
