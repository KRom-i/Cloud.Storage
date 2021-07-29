package Files.IncomigFiles;

import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class ListIncomingFile extends Stage {

    public ListIncomingFile (List<IncomingFile> incomingFiles, Node node, Cloud cloud) {

        VBox mainVBox = new VBox();
        mainVBox.setPadding(new Insets(5,5,5,5));
        mainVBox.setSpacing(15);

        mainVBox.getChildren().addAll(incomingFiles);

        Button button = new Button("ОК");
        button.setOnAction(event -> {
            cloud.downloadInFiles(incomingFiles);
            close();
        });

        HBox hBoxButton = new HBox();
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxButton.getChildren().add(button);

        mainVBox.getChildren().add(hBoxButton);
        mainVBox.getStylesheets().add("style.css");

        Scene scene = new Scene (mainVBox);
        scene.setFill(Color.TRANSPARENT);

        Stage mainStage = (Stage) node.getScene().getWindow();

        setScene (scene);
        initModality (Modality.WINDOW_MODAL);
        initOwner (mainStage);
        initStyle (StageStyle.UTILITY);
        setTitle("Поступившие файлы от пользователей");
        show();


    }


}
