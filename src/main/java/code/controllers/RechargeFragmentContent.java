package code.controllers;

import code.DBServer;
import code.InvalidateEventListner;
import code.entity.BankAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RechargeFragmentContent {

    public VBox root;
    public ChoiceBox accountChoiceBox;
    public TextField amountTF;
    private DBServer.BankAccounts bankAccounts;
    private InvalidateEventListner listner;

    public RechargeFragmentContent() {

    }

    void fillInInformation(ArrayList<BankAccount> bankAccounts) {
        ObservableList<BankAccount> accounts = FXCollections.observableArrayList(bankAccounts);
        accountChoiceBox.setItems(accounts);
        accountChoiceBox.getSelectionModel().selectFirst();

    }

    public void replenishBtOnClick(ActionEvent actionEvent) {
        BankAccount bankAccount = (BankAccount) accountChoiceBox.getSelectionModel().getSelectedItem();
        try {
            float ammount = Float.valueOf(amountTF.getText());
            if (ammount <= 0)
                throw new NumberFormatException();
            amountTF.setStyle(null);
            bankAccounts.replanishAcc(bankAccount, ammount);
            clear();
            listner.invalidate();
        } catch (NumberFormatException e) {
            amountTF.setStyle("-fx-border-color:red;");
        }

    }

    void clear() {
        amountTF.clear();
    }

    @FXML
    void initialize() {
        DBServer database = new DBServer();
        bankAccounts = database.new BankAccounts();
    }

    public void setInvalidateEventListner(InvalidateEventListner listner) {
        this.listner = listner;
    }
}
