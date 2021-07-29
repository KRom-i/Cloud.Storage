package GraphicalIinterface.CommonСlassesGUI.DownloadHistory;

import GraphicalIinterface.CommonСlassesGUI.Img.CustomImg;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DownloadInfo extends VBox {


    private long sizeFile;
    private int sizeDownload = 0;


    public DownloadInfo () {

        Button buttonClose = new Button();
        new CustomImg("close", 10, 10).addButton(buttonClose);
        getChildren().add(buttonClose);

        buttonClose.setOnMouseClicked(event -> {
            Platform.runLater(()->{
                getChildren().clear();
                getChildren().add(buttonClose);
                setThisVisible(false);
            });
        });
    }

    public void setTextUpload(String filename, String infoPath){
        String text = String.format("Передача в Cloud storage [ %s ] %s", filename, infoPath);
        setText(text);
    }

    public void setTextUploadEnd(String filename, String infoPath){
        String text = String.format("Файла [ %s ] передан в Cloud storage %s ", filename, infoPath);
        setText(text);
    }

    public void setTextUploadError(String filename, String infoPath){
        String text = String.format("Ошибка при передаче файла [ %s ] %s", filename, infoPath);
        setText(text);
    }

    public void setTextDownload(String filename, String infoPath){
        String text = String.format("Загрузка файла [ %s ] %s", filename, infoPath);
        setText(text);
    }

    public void setTextDownEnd(String filename, String infoPath){
        String text = String.format("Файла [ %s ] загружен %s", filename, infoPath);
        setText(text);
    }

    public void setTextDownError(String filename, String infoPath){
        String text = String.format("Ошибка при загрузке файла [ %s ] %s", filename, infoPath);
        setText(text);
    }

    private void setText(String text){
        Platform.runLater(()-> {
            setThisVisible(true);
            int size = getChildren().size();
            Label labelFile = (Label) ((HBox) getChildren().get(size - 1)).getChildren().get(1);
            labelFile.setText(text);
        });
    }
    public void show(long sizeFile){
        this.sizeFile = sizeFile;
        Platform.runLater(()->{
            setThisVisible(true);
            HBox hBoxInfo = new HBox();
            hBoxInfo.setSpacing(5);
            hBoxInfo.getChildren().addAll(new ProgressBar(), new Label());
            getChildren().add(hBoxInfo);
        });

    };

    public void setProgress (int bytes) {
        Platform.runLater(()->{
        this.sizeDownload += bytes;

        double value = 0;

        if (sizeDownload > sizeFile){
            value = 1;
        } else {
            value = (double) (sizeFile / sizeDownload) / 100;
        }

            int size = getChildren().size();
            ProgressBar progressBarDownload = (ProgressBar) ((HBox) getChildren().get(size - 1)).getChildren().get(0);
            progressBarDownload.setProgress(value);

        });
    }

    public void setSave (String fileName) {
        Platform.runLater(()->{
            getChildren().add(new HBox(new Label(String.format("Файла [ %s ] сохранен ", fileName))));
            setThisVisible(true);
        });

    }

    public void setThisVisible(boolean value){
        setVisible(value);
        setManaged(value);
    }


}
