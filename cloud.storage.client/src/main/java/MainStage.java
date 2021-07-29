import Logger.Log;
import GraphicalIinterface.MainUserInterface.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage extends Application {

    private MainBox mainBox;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainBox = new MainBox();
        primaryStage.setScene(new Scene(mainBox, 1600, 860));
        primaryStage.setTitle("Cloud storage");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop () throws Exception {
        mainBox.closeClouds();
        Log.info("Close");
        super.stop();
    }

}
