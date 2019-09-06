package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	

	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("PDFTesterForm.fxml"));
			Scene scene = new Scene(root,600,650);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UTILITY);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Simple PDF Tester");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
