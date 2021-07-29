package Files.IncomigFiles;

import Commands.Commands;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class IncomingFile extends VBox {

    private String name;
    private long size;
    private String fromUser;
    private CheckBox download;
    private CheckBox delete;



    public IncomingFile (String msg) {

        String[] info = msg.split(Commands.DELIMITER[0]);

        this.name = info[1];
        this.size = Long.parseLong(info[2]);
        this.fromUser = info[3];


        HBox hBoxCheck = new HBox();
        hBoxCheck.setSpacing(5);

        this.download = new CheckBox("Загрузить");
        this.download.setSelected(true);
        this.download.setOnMouseClicked(event -> {
            if (download.isSelected()) delete.setSelected(false);
        });

        this.delete = new CheckBox("Удалить");
        this.delete.setSelected(false);
        this.delete.setOnMouseClicked(event -> {
            if (delete.isSelected()) download.setSelected(false);
        });


        hBoxCheck.getChildren().addAll(download, delete);

        Label labelNameUser  = new Label(String.format("Отправитель [ %s ]", fromUser));
        labelNameUser.getStyleClass().add("cloud-label");

        Label labelNameFile = new Label(String.format("Файл [ %s ]", name));
        labelNameFile.setMaxWidth(400);

        Label labelSizeFile = new Label(String.format("Размер файла [ %s ]", size));

        getChildren().addAll(labelNameUser, labelNameFile, labelSizeFile, hBoxCheck);

        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);

        setSpacing(5);
    }


    public String getCmd(){
        if (download.isSelected()) delete.setSelected(true);

        return Commands.BUILD(0, Commands.FILE, Commands.INCOMING, Commands.GET,
                              Commands.BUILD(1, name, fromUser, String.valueOf(download.isSelected()), String.valueOf(delete.isSelected())));
    }
    @Override
    public String toString() {
        return String.format("IncomingFile:%s:%s:%s:%s", name, size, fromUser, download.isSelected() );
    }
}
