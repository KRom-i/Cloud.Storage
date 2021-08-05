package GraphicalIinterface.CommonСlassesGUI.ShowFiles;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class ShowMedia extends VBox {

    private File file;
    private Boolean delete;

    public ShowMedia (File file, boolean delete) {

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

        Media media = new Media(file.toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitHeight(400);
        mediaView.setFitWidth(400);

        setSpacing(5);
        setPadding(new Insets(5, 5, 5, 5));

        VBox vBoxContent = new VBox();
        vBoxContent.setAlignment(Pos.CENTER);
        vBoxContent.getChildren().add(mediaView);

        getChildren().addAll(boxButtons, vBoxContent, hBoxInfo);

        HBox.setHgrow(vBoxContent, Priority.ALWAYS);
        VBox.setVgrow(vBoxContent, Priority.ALWAYS);

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
