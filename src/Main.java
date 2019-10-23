import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("vista/inicio.fxml"));
        primaryStage.setTitle("Inteligencia Artificial - Foraging");
        primaryStage.setScene(new Scene(root, 1350, 710));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
