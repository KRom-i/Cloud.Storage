package GraphicalIinterface.CommonСlassesGUI.ListFiles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ButtonsSort extends HBox {


    public ButtonsSort (ListFiles listFiles) {

        SortBox sortBoxName = new SortBox("Имя файла");
        sortBoxName.setMinWidth(400);
        sortBoxName.setMaxWidth(400);
        sortBoxName.setAlignment(Pos.CENTER);
        sortBoxName.setOnMouseClicked(event -> {
            listFiles.sortName(sortBoxName.isClick());
            sortBoxName.setClick(!sortBoxName.isClick());
        });


        SortBox sortBoxDate = new SortBox("Дата изменения");
        sortBoxDate.setMinWidth(100);
        sortBoxDate.setAlignment(Pos.CENTER);
        sortBoxDate.setOnMouseClicked(event -> {
            listFiles.sortDate(sortBoxName.isClick());
            sortBoxName.setClick(!sortBoxName.isClick());
        });


        SortBox sortBoxType = new SortBox("Тип файла");
        sortBoxType.setMinWidth(100);
        sortBoxType.setAlignment(Pos.CENTER);
        sortBoxType.setOnMouseClicked(event -> {
            listFiles.sortType(sortBoxName.isClick());
            sortBoxName.setClick(!sortBoxName.isClick());
        });


        SortBox sortBoxSize = new SortBox("Размер");
        sortBoxSize.setMinWidth(100);
        sortBoxSize.setAlignment(Pos.CENTER);
        sortBoxSize.setOnMouseClicked(event -> {
            listFiles.sortSize(sortBoxName.isClick());
            sortBoxName.setClick(!sortBoxName.isClick());
        });


        setSpacing(2.5);
        setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(sortBoxName,sortBoxDate,sortBoxType, sortBoxSize);


    }


    private class SortBox extends HBox{

        private boolean click;

        public SortBox (String name) {

            this.click = false;

            setOpacity(0.7);

            setOnMouseEntered(event -> setOpacity(1));
            setOnMouseExited(event -> setOpacity(0.7));

            Label labelSort = new Label(name);
            labelSort.getStyleClass().add("label-sort");

            getChildren().add(labelSort);

            VBox.setVgrow(this, Priority.NEVER);
            HBox.setHgrow(this, Priority.ALWAYS);

            getStyleClass().add("box-sort");
        }

        public boolean isClick () {
            return click;
        }

        public void setClick (boolean click) {
            this.click = click;
        }
    }




}
