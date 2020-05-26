package code.controllers;

import code.entity.Bundle;
import code.DBServer;
import code.Transition;
import code.Verificator;
import code.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class SignInController {

    DBServer.Users users;
    Text errorText = new Text();

    @FXML
    private AnchorPane form;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInBt;

    @FXML
    private Text goToRegistration;

    @FXML
    void registrationTxtClick(MouseEvent event) throws IOException {
        Transition.hideWindow(goToRegistration);
        Transition.openWindow(getClass().getResource("/layouts/registrationForm.fxml"), false);
    }


    void showErrorText(String txt) {
        errorText.setText(txt);
        double etWidth = errorText.getLayoutBounds().getWidth();
        errorText.setFill(Paint.valueOf("#ff0000"));
        errorText.setLayoutX((form.getWidth() - etWidth) / 2);
        errorText.setLayoutY(144.0);
    }

    @FXML
    void signInBtClick(MouseEvent event) throws IOException {
        if (Verificator.checkFillTextField(new TextField[]{loginField, passwordField})) {
            URL mainFormURL = getClass().getResource("/layouts/MainForm.fxml");
            User user = users.select(loginField.getText(), passwordField.getText());
            if (user != null) {
                Transition.hideWindow(signInBt);
                Bundle bundle = new Bundle();
                bundle.setBundle("User", user);

                MainForm mainForm = Transition.openWindow(mainFormURL, true);
                Transition.setBundle(bundle, mainForm);
            } else {
                showErrorText("Пользователь не найден!");
            }
        }
    }

    @FXML
    void initialize() {
        form.getChildren().add(errorText);
        DBServer database = new DBServer();
        users = database.new Users();
    }


}
