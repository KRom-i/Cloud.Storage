package GraphicalIinterface.CommonСlassesGUI.DialogStage;

import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDelete extends Stage{



    public QuestionDelete (ManagerBox managerBox, List<FileInfo> listFileInfo, double corX, double corY ) {

        VBox mainVBox = new VBox();
        mainVBox.setPadding(new Insets(5, 5, 5, 5));
        mainVBox.setMinWidth(350);

        try {
            ImageView imageView1 = new ImageView(new javafx.scene.image.Image(new File("img/logo/question.png").toURI().toURL().toString()));
            imageView1.setFitHeight(40);
            imageView1.setFitWidth(40);
            mainVBox.getChildren().addAll(imageView1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (listFileInfo.size() > 10) {

            ListView<Label> hBoxListView = new ListView<>();

            for(FileInfo file: listFileInfo) {
                hBoxListView.getItems().addAll(file.getLabelQuestionDelete()); }
            mainVBox.getChildren().addAll(hBoxListView);

        } else {
            for(FileInfo file: listFileInfo) {
                mainVBox.getChildren().addAll(file.getLabelQuestionDelete());
            }
        }


        Button buttonNo = new Button("Отмена");
        buttonNo.setMinWidth(80);
        buttonNo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                close();
            }
        });

        Button buttonYes = new Button("Удалить");
        buttonYes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
               managerBox.delete(listFileInfo);
               close();
            }
        });
        buttonYes.setMinWidth(80);

        HBox hBoxButtons = new HBox();

        hBoxButtons.setSpacing(5);
        hBoxButtons.setAlignment(Pos.BOTTOM_RIGHT);


        hBoxButtons.getChildren().addAll(buttonNo, buttonYes);

        hBoxButtons.setPadding(new Insets(5, 5, 5, 5));

        mainVBox.setSpacing(5);
        mainVBox.getStylesheets().add("style.css");
        mainVBox.getChildren().addAll(hBoxButtons);

        Scene scene = new Scene (mainVBox);
        scene.setFill(Color.TRANSPARENT);

        Stage mainStage = (Stage) managerBox.getScene().getWindow();

        setScene (scene);
        initModality (Modality.WINDOW_MODAL);
        initOwner (mainStage);
        initStyle (StageStyle.UTILITY);
        setTitle("Удаление файлов");
        setX(corX);
        setY(corY);
        show();


    }


}
