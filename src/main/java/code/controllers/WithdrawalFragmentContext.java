package code.controllers;

import code.DBServer;
import code.InvalidateEventListner;
import code.entity.BankAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class WithdrawalFragmentContext {

    public AnchorPane root;
    public ChoiceBox accountChoiceBox;
    public TextField amountTF;
    public ComboBox categoriesCB;
    public TextArea descriptionTA;
    public Button withdrawalBt;
    private DBServer.BankAccounts bankAccounts;
    private DBServer.Categories categories;
    private DBServer.Expenses expenses;
    private InvalidateEventListner listner;

    void fillInInformation(ArrayList<BankAccount> bankAccounts) {
        ObservableList<BankAccount> accounts = FXCollections.observableArrayList(bankAccounts);
        accountChoiceBox.setItems(accounts);
        accountChoiceBox.getSelectionModel().selectFirst();
    }


    void clear() {
        amountTF.clear();
        descriptionTA.clear();
    }

    @FXML
    void initialize() {
        DBServer database = new DBServer();
        bankAccounts = database.new BankAccounts();
        expenses = database.new Expenses();
        categories = database.new Categories();
        categoriesCB.setItems(FXCollections.observableList(categories.selectAll()));

    }

    public void setInvalidateEventListner(InvalidateEventListner listner) {
        this.listner = listner;
    }

    public void withdrawalBtOnClick(ActionEvent actionEvent) {
        BankAccount bankAccount = (BankAccount) accountChoiceBox.getSelectionModel().getSelectedItem();
        if (categoriesCB.getSelectionModel().getSelectedItem() != null) {
            categoriesCB.setStyle("-fx-border-color:transparent;");
            try {
                float ammount = Float.valueOf(amountTF.getText());
                if (ammount <= 0)
                    throw new NumberFormatException();
                amountTF.setStyle(null);
                bankAccounts.replanishAcc(bankAccount, -ammount);
                expenses.insert((String) categoriesCB.getSelectionModel().getSelectedItem(),
                        ammount, bankAccount.getId(), descriptionTA.getText());
                clear();
                listner.invalidate();
            } catch (NumberFormatException e) {
                amountTF.setStyle("-fx-border-color:red;");
            }
        } else {
            categoriesCB.setStyle("-fx-border-color:red;");
        }
    }
}
