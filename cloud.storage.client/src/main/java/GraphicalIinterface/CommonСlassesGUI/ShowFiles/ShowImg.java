package GraphicalIinterface.CommonСlassesGUI.ShowFiles;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;

public class ShowImg extends VBox {

    private File file;
    private Boolean delete;

    public ShowImg (File file, boolean delete) {

        this.file = file;
        this.delete = delete;



        Button butClose = new Button("Закрыть");
        butClose.setOnAction(event -> close());

        HBox boxButtons = new HBox();
        boxButtons.setAlignment(Pos.CENTER_RIGHT);
        boxButtons.setSpacing(5);
        VBox.setVgrow(boxButtons, Priority.NEVER);
        boxButtons.getChildren().addAll(butClose);

        Label labelFileName = new Label("Файл - " + file.getName());
        labelFileName.setMaxWidth(350);

        HBox hBoxInfo = new HBox();
        hBoxInfo.setAlignment(Pos.CENTER_LEFT);
        hBoxInfo.setSpacing(5);
        VBox.setVgrow(hBoxInfo, Priority.NEVER);
        hBoxInfo.getChildren().add(labelFileName);



        VBox hBoxContent = new VBox();
        hBoxContent.setAlignment(Pos.CENTER);

        try {
            ImageView imageView = new ImageView(new Image(new File(file.getAbsolutePath()).toURI().toURL().toString()));
            imageView.setFitHeight(400);
            imageView.setFitWidth(400);
            hBoxContent.getChildren().add(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getChildren().addAll(boxButtons, hBoxContent, hBoxInfo);

        setSpacing(5);
        setPadding(new Insets(5, 5, 5, 5));

        HBox.setHgrow(hBoxContent, Priority.ALWAYS);
        VBox.setVgrow(hBoxContent, Priority.ALWAYS);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);

        getStylesheets().add("style.css");
        getStyleClass().add("box-show-file");

    }

    public void close () {
        Platform.runLater(()->{
            getChildren().clear();
        });
        setVisible(false);
        setManaged(false);
        if (delete && file.exists()) file.delete();
    }
}
