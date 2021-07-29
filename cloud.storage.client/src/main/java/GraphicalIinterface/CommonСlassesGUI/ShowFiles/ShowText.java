package GraphicalIinterface.CommonСlassesGUI.ShowFiles;

import Configuration.Config;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuCharset;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ShowText extends VBox {

    private File file;
    private String[] charset = Config.CHARSET;
    private TextArea textAreaFile;
    private Boolean delete;
    private HBox boxButtons;

    public ShowText (File file, String charsetName, boolean delete, boolean buttonsVis) {

        this.file = file;
        this.delete = delete;

        String charsetNameShow;
        if (charsetName == null){
            charsetNameShow = charset[0];
        } else {
            charsetNameShow = charsetName;
        }


        boxButtons = new HBox();
        boxButtons.setAlignment(Pos.CENTER_RIGHT);
        boxButtons.setSpacing(5);



        Button butStageShow = new Button("Открыть в отдельном окне");
        butStageShow.setOnAction(event -> {{
            close(false);
            new ShowStage(file, delete);
        } });


        Button butClose = new Button("Закрыть");
        butClose.setOnAction(event -> close());

        boxButtons.getChildren().addAll(butStageShow, butClose);
        VBox.setVgrow(boxButtons, Priority.NEVER);

        boxButtons.setVisible(buttonsVis);
        boxButtons.setManaged(buttonsVis);

        Label labelCharsetName = new Label("Кодировка");

        TextField tfCharsetName = new TextField(charsetNameShow);
        tfCharsetName.setEditable(false);
        tfCharsetName.setMaxWidth(150);
        tfCharsetName.setOnMouseClicked(event -> new MenuCharset(this, tfCharsetName, charset));

        Label labelFileName = new Label("Файл - " + file.getName());
        labelFileName.setMaxWidth(350);

        HBox hBoxInfo = new HBox();
        hBoxInfo.setAlignment(Pos.CENTER_LEFT);
        hBoxInfo.setSpacing(10);
        VBox.setVgrow(hBoxInfo, Priority.NEVER);
        hBoxInfo.getChildren().addAll(labelCharsetName, tfCharsetName,
                                      labelFileName);

        textAreaFile = new TextArea();
        textAreaFile.setEditable(false);
        textAreaFile.getStyleClass().add("text-aria");


        setSpacing(5);
        setPadding(new Insets(5,5,5,5));
        getChildren().addAll(boxButtons, hBoxInfo, textAreaFile);

        HBox.setHgrow(textAreaFile, Priority.ALWAYS);
        VBox.setVgrow(textAreaFile, Priority.ALWAYS);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);

        read(charsetNameShow);

        getStylesheets().add("style.css");
        getStyleClass().add("box-main");
    }

    public void read(String charsetName){

        textAreaFile.clear();

        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charsetName);
                BufferedReader reader = new BufferedReader(inputStreamReader);
        ){

            String line;
            while ((line = reader.readLine()) != null) {
                textAreaFile.appendText(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        Platform.runLater(()->{
            getChildren().clear();
        });
        setVisible(false);
        setManaged(false);
        if (delete && file.exists()) file.delete();
    }

    public void close(boolean delete){
        Platform.runLater(()->{
            getChildren().clear();
        });
        setVisible(false);
        setManaged(false);
        if (delete && file.exists()) file.delete();
    }



}
