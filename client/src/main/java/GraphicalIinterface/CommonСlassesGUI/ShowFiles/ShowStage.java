package GraphicalIinterface.CommonСlassesGUI.ShowFiles;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class ShowStage extends Stage {

    public ShowStage (File file, boolean delete) {
        Platform.runLater(()->{
            Scene scene = new Scene (new ShowText(file, null, delete, false), 800, 800);
            scene.setFill(Color.TRANSPARENT);
            setScene (scene);
            initStyle (StageStyle.UTILITY);
            setTitle("Stage show file");
            show();
        });

    }
}
