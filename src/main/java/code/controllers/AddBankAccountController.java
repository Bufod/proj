package code.controllers;

import code.DBServer;
import code.InvalidateEventListner;
import code.entity.User;
import code.Verificator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddBankAccountController {

    @FXML
    public VBox root;
    @FXML
    private TextField bankName;

    @FXML
    private TextField accName;

    @FXML
    private TextField balance;

    private InvalidateEventListner listner;

    private DBServer.BankAccounts bankAccounts;
    private User user;

    public void setInvalidateEventListner(InvalidateEventListner listner) {
        this.listner = listner;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void createBtOnClick(ActionEvent event) {
        if (Verificator.checkFillTextField(new TextField[]{bankName, accName, balance})) {
            bankAccounts.insert(bankName.getText(), accName.getText(),
                    Float.valueOf(balance.getText()), user.getId());
            listner.invalidate();
        }
    }

    @FXML
    void initialize() {
        DBServer database = new DBServer();
        bankAccounts = database.new BankAccounts();
    }

}