package code.controllers;

import code.DBServer;
import code.Transition;
import code.Verificator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;

public class RegistrationForm {

    DBServer.Users users;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInBt;

    @FXML
    private TextField nameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordFieldRepeat;

    boolean checkPasswordFields(TextField pass1, TextField pass2) {
        if (pass1.getText().equals(
                pass2.getText())) {
            pass1.setStyle("-fx-border-color:green;");
            pass2.setStyle("-fx-border-color:green;");
            return true;
        } else {
            pass1.setStyle("-fx-border-color:red;");
            pass2.setStyle("-fx-border-color:red;");
            return false;
        }
    }

    @FXML
    void signUpBt(MouseEvent event) throws IOException {
        if (Verificator.checkFillTextField(new TextField[]{nameField, lastNameField, loginField,
                passwordField, passwordFieldRepeat}) &&
                checkPasswordFields(passwordField,
                        passwordFieldRepeat)
        ) {
            File config = new File("config.txt");
            if (!config.exists()) {
                if (!config.createNewFile())
                    throw new IOException("Не удалось создать файл");
            }

            users.insert(nameField.getText(), lastNameField.getText(), loginField.getText(),
                    passwordField.getText());

            Transition.hideWindow(signInBt);
            Transition.openWindow(getClass().getResource("/layouts/sample.fxml"), false);
        }

    }

    @FXML
    void initialize() {
        DBServer database = new DBServer();
        users = database.new Users();
    }
}
