package GraphicalIinterface.GUIBoxes.CloudBox.AuthBox;

import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.CommonСlassesGUI.Img.CustomImg;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AuthBox extends VBox {

    private Cloud cloud;
    private HBox hBoxNickName;
    private TextField nickNameField;
    private TextField loginField;
    private TextField passwordField;

    private Button buttonAuth;
    private Button buttonReg;
    private Button buttonCancel;

    private Label labelError;

    private AuthBuf authBuf;

    public AuthBox (Cloud cloud) {

        this.cloud = cloud;

        setMaxSize (350,100);
        setPadding(new Insets(5, 5, 5, 5));
        setAlignment (Pos.CENTER_RIGHT);
        setSpacing (2);

        hBoxNickName = new HBox();
        hBoxNickName.setSpacing(15);
        hBoxNickName.setAlignment(Pos.CENTER);
        hBoxNickName.setPadding(new Insets(5,5,5,5));

        ImageView nickImage = new CustomImg("user", 25, 25, hBoxNickName);

        nickNameField = new TextField();
        nickNameField.setAlignment(Pos.CENTER);
        nickNameField.setPromptText ("Имя пользователя");
        nickNameField.setMinSize (300,40);
        nickNameField.setPadding(new Insets(5,5,5,5));

        hBoxNickName.getChildren().addAll(nickImage, nickNameField);

        HBox hBoxLog = new HBox();
        hBoxLog.setSpacing(15);
        hBoxLog.setAlignment(Pos.CENTER);
        hBoxLog.setPadding(new Insets(5,5,5,5));

        loginField = new TextField();
        loginField.setAlignment(Pos.CENTER);
        loginField.setPromptText ("Логин");
        loginField.setMinSize (300,40);
        loginField.setPadding(new Insets(5,5,5,5));

        ImageView userImage = new CustomImg("user", 25, 25, hBoxLog);

        hBoxLog.getChildren().addAll(userImage, loginField);

        HBox hBoxPass = new HBox();
        hBoxPass.setSpacing(15);
        hBoxPass.setAlignment(Pos.CENTER);
        hBoxPass.setPadding(new Insets(5,5,5,5));

        passwordField = new PasswordField();
        passwordField.setAlignment(Pos.CENTER);
        passwordField.setPromptText ("Пароль");
        passwordField.setMinSize (300,40);
        passwordField.setPadding(new Insets(5,5,5,5));

        ImageView passImage = new CustomImg("password", 25, 25, hBoxPass);

        hBoxPass.getChildren().addAll(passImage, passwordField);

        buttonAuth = new Button ("Вход");
        buttonAuth.setMinWidth(300);

        buttonReg = new Button ("Добавить пользователя");
        buttonReg.setOnMouseClicked((event -> {

            if (!buttonCancel.isVisible()){
                initReg(true);

            } else {
                labelError.setText(reg());
            }
        }));

        buttonReg.setMinWidth(300);

        buttonCancel = new Button("Отмена");
        buttonCancel.setOnAction(event -> initReg(false));
        buttonCancel.setMinWidth(300);

        VBox vBoxButtons = new VBox();
        vBoxButtons.setSpacing(5);
        vBoxButtons.setPadding(new Insets(5,5,5,5));
        vBoxButtons.setAlignment(Pos.CENTER_RIGHT);
        vBoxButtons.getChildren().addAll(buttonAuth, buttonReg, buttonCancel);

        labelError = new Label();

        getChildren ().addAll (hBoxNickName, hBoxLog,  hBoxPass, vBoxButtons, labelError);
        nodesVis(false, hBoxNickName, buttonCancel);

        loginField.setOnAction (event -> labelError.setText(auth()));
        passwordField.setOnAction (event -> labelError.setText(auth()));
        buttonAuth.setOnAction (event -> labelError.setText(auth()));

        if (authBuf != null){
            loginField.setText(AuthBuf.log);
            passwordField.setText(AuthBuf.pass);
        }
    }

    private String reg () {

        String nickName = nickNameField.getText();
        String login = loginField.getText();
        String pass = passwordField.getText();

        String strError = "менее 4х символов";

        if (nickName.length() < 4) return "Имя " + strError;

        if (login.length() < 4) return "Логин " + strError;

        if (pass.length() < 4) return "Пароль " + strError;

        cloud.reg(nickName, login, pass.hashCode());
        return "";
    }

    private void initReg(boolean value){
        nodesVis(value, hBoxNickName, buttonCancel);
        nodesVis(!value, buttonAuth);
    }

    private String auth() {

        String log = loginField.getText();
        String pas = passwordField.getText();

        String strError = "менее 4х символов";

        if (log.length() < 4) return "Логин " + strError;

        if (pas.length() < 4) return "Пароль " + strError;

        AuthBuf.log = log;
        AuthBuf.pass = pas;

        cloud.auth(log, pas.hashCode());
        return "";

    }


    private void nodesVis(boolean value, Node... nodes){

        for (Node n: nodes
        ) {

            n.setVisible(value);

            n.setManaged(value);
        }
    }

    public void setInfo (int check) {

        String text = "";

        switch (check) {
            case -3:
                text = "Неверный логин или пароль";
                break;
            case -2:
                text = "Логин уже зарегистрирован !";
                break;
            case -1:
                text = "Имя пользователя  уже зарегистрировано !";
                break;
            case 0:
                text = "Ошибка при регистрации пользователя !";
                break;
            case 1:
                text = "Пользователь зарегистрирован !";
                initReg(false);
                break;

        }

        String finalText = text;

        Platform.runLater(()->{
            labelError.setText(finalText);
        });
    }




}
