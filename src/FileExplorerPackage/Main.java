package FileExplorerPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Parent parent;
    public static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        parent = FXMLLoader.load(getClass().getResource("TableView.fxml"));
        //parent.getStylesheets().add("viper.css");
        window.setTitle("File Explorer");
        window.setScene(new Scene(parent, 765.0, 463.0));
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
