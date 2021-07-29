package GraphicalIinterface.GUIBoxes.CloudBox;

import GraphicalIinterface.CommonСlassesGUI.Img.CustomImg;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuOptionCloud;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class InfoCloud extends HBox {

    private Cloud cloud;
    private Label labelSize;
    private Button butOption;
    private Button butInFile;

    public ProgressBar progressBar;


    public InfoCloud (Cloud cloud) {

        this.cloud = cloud;

        this.progressBar = new ProgressBar();
        progressBar.setLayoutX(1);
        progressBar.setScaleX(1);
        progressBar.setTranslateX(1);
        progressBar.setMaxWidth(Double.MAX_VALUE);

        labelSize = new Label();

        butOption = new Button();
        new CustomImg("cloud", 20, 30).addButton(butOption);
        butOption.setOnAction(event -> new MenuOptionCloud(cloud, butOption));

        butInFile = new Button();
        new CustomImg("msg", 20,20).addButton(butInFile);
        butInFile.setOnAction(event -> cloud.getListIncomingFiles());
        nodesVis(false, butInFile);


        setSpacing(5);
        setPadding(new Insets(0,15,0,15));
        setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(labelSize, progressBar, butOption, butInFile);

        HBox.setHgrow(progressBar, Priority.ALWAYS);
        VBox.setVgrow(progressBar, Priority.NEVER);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.NEVER);



        update();
        setNumInFiles(0);
    }


    public void update(){
        Platform.runLater(()->{
            progressBar.setProgress(cloud.getSizePercent());
            labelSize.setText(cloud.sizeToString());
        });
    };

    public void setNumInFiles(int i){
        Platform.runLater(()->{
        if (i == 0){
            nodesVis(false, butInFile);
        } else {
            nodesVis(true, butInFile);
            butInFile.setText(i + "");
        }
        });
    }


    public void nodesVis(boolean value, Node... nodes){
        for (Node n: nodes
        ) {
            n.setVisible(value);
            n.setManaged(value);
        }
    }

}
