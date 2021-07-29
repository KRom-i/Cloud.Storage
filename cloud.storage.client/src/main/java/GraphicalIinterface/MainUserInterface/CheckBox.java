package GraphicalIinterface.MainUserInterface;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class CheckBox extends VBox {

    public String[] checkFields = {
            "ОБЗОР ФУНКЦИОНАЛА",

            "Аутентификация пользователя",
            "Добавление пользователя",

            "Навигация",
            "Сортировка файлов",
            "Поиск файла/директорию",

            "Загрузка файлов на сервер",
            "Фиксированный размер хранилища",

            "Загрузка файлов на клиента",

            "Создать директорию",
            "Создать файл",
            "Переименовать файл/директорию",

            "Копировать файл",
            "Создать архив",
            "Удалить файл",
            "Распаковать архив",

            "Отправка файлов др. пользователю",

            "Предварительный просмотр"};



    public CheckBox () {

        Button[] buttons = new Button[getCheckFields ().length];

        for (int i = 0; i < buttons.length; i++) {

            buttons[i] = new Button(checkFields[i]);
            buttons[i].setId("But" + i);

            buttons[i].setOpacity(0.5);

            buttons[i].setMinHeight(30);

            buttons[i].setMinWidth(250);
            final int I = i;
            buttons[i].setOnAction(event -> {

                buttons[I].setOpacity(1);

                buttons[I].setMinHeight(45);

                for (Button b: buttons
                     ) {
                    if (!b.equals(buttons[I]) && !b.getId().equals("But0")) {
                        b.setMinHeight(30);
                    }
                }
            });

        }


        getChildren().addAll(buttons);

        setSpacing(10);
        setPadding(new Insets(10,10,10,10));
    }


    public String[] getCheckFields () {
        return checkFields;
    }
}
