package GraphicalIinterface.CommonСlassesGUI.DialogStage;

import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class DialogOutFile extends Stage {

    private TextField tfNameUser;
    private Cloud cloud;
    private Label labelInfoOperation;
    private Button buttonNo;
    private Button buttonYes;
    private List<FileInfo> fileInfoList;

    public DialogOutFile (Cloud cloud, List<FileInfo> fileInfoList) {

        this.cloud = cloud;
        this.fileInfoList = fileInfoList;


        Label[] labelFiles = new Label[fileInfoList.size()];

        for (int i = 0; i < labelFiles.length; i++) {
            labelFiles[i] = new Label(fileInfoList.get(i).getFullName());
            labelFiles[i].setMaxWidth(450);
            labelFiles[i].getStyleClass().add("cloud-label");
        }


        Label labelNameUser = new Label("Имя пользователя");

        tfNameUser = new TextField();
        tfNameUser.setMaxWidth(300);
        tfNameUser.setPromptText("Ввод");

        labelInfoOperation = new Label();

        buttonNo = new Button("Отмена");
        buttonNo.setMinWidth(80);
        buttonNo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                close();
            }
        });

        buttonYes = new Button("Отправить");
        buttonYes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                check();
            }
        });
        buttonYes.setMinWidth(80);

        HBox hBoxButtons = new HBox();

        hBoxButtons.setSpacing(5);
        hBoxButtons.setAlignment(Pos.BOTTOM_RIGHT);

        HBox.setHgrow(hBoxButtons, Priority.ALWAYS);
        VBox.setVgrow(hBoxButtons, Priority.NEVER);

        hBoxButtons.getChildren().addAll(labelInfoOperation, buttonNo, buttonYes);
        hBoxButtons.setPadding(new Insets(5, 5, 5, 5));

        VBox vBoxContent = new VBox();

        vBoxContent.getChildren().addAll(labelFiles);
        vBoxContent.getChildren().addAll(labelNameUser, tfNameUser, hBoxButtons);
        vBoxContent.getStylesheets().add("style.css");
        vBoxContent.setSpacing(5);
        vBoxContent.setPadding(new Insets(10,10,10,10));
        vBoxContent.getStyleClass().add("box-manager");

        Scene scene = new Scene (vBoxContent);
        scene.setFill(Color.TRANSPARENT);

        Stage mainStage = (Stage) fileInfoList.get(0).getTextField().getScene().getWindow();

        setScene (scene);
        initModality (Modality.WINDOW_MODAL);
        initOwner (mainStage);
        initStyle (StageStyle.UTILITY);
        setTitle(cloud.getNickName() + " отправка файла");
        show();


    }

    public boolean checkNick(String nick){
        return tfNameUser.getText().equalsIgnoreCase(nick);
    }

    private void check(){

        if (checkNick(cloud.getNickName())){
            labelInfoOperation.setText("Имя получателя совпадает с именем отправителя !");
        } else {
            cloud.outNickName(tfNameUser.getText());
            setEdit(false);
        }

    }

    public void errorOut(){
        Platform.runLater(()->{
            labelInfoOperation.setText("Ошибка при отправке файла !");
            nodesVis(true, buttonNo, buttonYes);
            setEdit(true);
        });
    }

    public void errorNick(){
        Platform.runLater(()->{
            labelInfoOperation.setText("Пользователь не найден !");
            setEdit(true);
        });
    }

    public void fileSent () {
        Platform.runLater(()->{
            labelInfoOperation.setText("Файл отправлен !");
            nodesVis(false, buttonNo, buttonYes);
        });

    }

    public void setEdit(boolean value){
        tfNameUser.setEditable(value);
    }

    public List<FileInfo> getFileInfoList () {
        return fileInfoList;
    }

    public void nodesVis(boolean value, Node... nodes){
        for (Node n: nodes
        ) {
            n.setVisible(value);
            n.setManaged(value);
        }
    }


}
