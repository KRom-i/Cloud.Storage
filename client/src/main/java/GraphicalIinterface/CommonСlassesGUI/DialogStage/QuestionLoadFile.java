package GraphicalIinterface.CommonСlassesGUI.DialogStage;

import TransportPackage.TransportPackage;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class QuestionLoadFile  {

    static private Label labelNamePackage;
    static private Label labelReplace;
    static private Label labelCopy;
    static private Label labelRename;
    static private TextField textFieldRename;


    public static void getUserCommand (TransportPackage transportPackage, Node node) {

        Stage stage = new Stage();

        labelNamePackage = new Label(transportPackage.getNamePack() + "\nфайл существует в папке назначения");

        labelReplace = new Label("Заменить файл");
        labelReplace.setOnMouseClicked(event -> {
            transportPackage.setUserCommand(transportPackage.packReplace());
            stage.close();
        });
        ;

        labelCopy = new Label("Создать копию");
        labelCopy.setOnMouseClicked(event -> {
            transportPackage.setUserCommand(transportPackage.packCopy());
            stage.close();
        });

        labelRename = new Label("Переименовать");

        textFieldRename = new TextField();
        textFieldRename.setOnAction(event -> {
            transportPackage.setUserCommand(transportPackage.packRename(textFieldRename.getText()));
            stage.close();
        });

        VBox mainVBox = new VBox();
        mainVBox.setPadding(new Insets(5, 5, 5, 5));
        mainVBox.setMinWidth(350);
        mainVBox.setSpacing(5);
        mainVBox.getChildren().addAll(labelNamePackage, labelReplace, labelCopy, labelRename, textFieldRename);

        Scene scene = new Scene(mainVBox);
        scene.setFill(Color.TRANSPARENT);

        Stage mainStage = (Stage) node.getScene().getWindow();

        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner (mainStage);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Действия с файлом");
        stage.show();

        stage.setOnCloseRequest(event -> {
            if (transportPackage.getUserCommand() == null){
                transportPackage.setUserCommand(transportPackage.packCopy());
            }
        });


    }





}
