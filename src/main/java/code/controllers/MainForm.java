package code.controllers;

import code.*;
import code.entity.BankAccount;
import code.entity.Bundle;
import code.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainForm implements Receivable, InvalidateEventListner {

    @FXML
    public AnchorPane menuPane;
    @FXML
    public Label fullName;
    @FXML
    public Button replanishAcc;
    @FXML
    public Button myWalletBt;
    @FXML
    public Button withdrawalBt;
    @FXML
    public Button expenseLogBt;
    @FXML
    public ImageView changeUser;
    @FXML
    public VBox contentPane;
    @FXML
    private ImageView circleFrame;

    private User user;
    private DBServer.BankAccounts bankAccounts;
    private DBServer.Users users;

    private WalletStructureContent walletStructureContent;
    private RechargeFragmentContent rechargeFragmentContent;
    private WithdrawalFragmentContext withdrawalFragmentContext;
    private ExpensesLogFragmentContent expensesLogFragmentContent;

    public void setBundle(Bundle bundle) {

        if (bundle != null && bundle.hasBundle("User")) {
            this.user = (User) bundle.getBundle("User");
            if (user != null && walletStructureContent != null) {
                fullName.setText(user.getLastname() + "\n" + user.getFirstname());
                invalidate();
            }
        }

    }

    void clearPane() {
        contentPane.getChildren().remove(contentPane.getChildren().size() - 1);
    }

    @FXML
    void onClickReplanishAcc(ActionEvent event) throws IOException {
        clearPane();
        resetSelectBt();
        replanishAcc.getStyleClass().add("button-selected");

        if (rechargeFragmentContent != null)
            Transition.integrate(contentPane, rechargeFragmentContent.root);
        else {
            rechargeFragmentContent = Transition.integrate(contentPane, "/layouts/recharge_fragment.fxml", this.getClass());
            rechargeFragmentContent.setInvalidateEventListner(this);
        }

        invalidate();
    }

    public void resetSelectBt() {
        replanishAcc.getStyleClass().remove("button-selected");
        myWalletBt.getStyleClass().remove("button-selected");
        withdrawalBt.getStyleClass().remove("button-selected");
        expenseLogBt.getStyleClass().remove("button-selected");
    }

    public void onClickMyWalletBt(ActionEvent actionEvent) throws IOException {
        clearPane();
        resetSelectBt();
        myWalletBt.getStyleClass().add("button-selected");

        if (walletStructureContent != null) {
            Transition.integrate(contentPane, walletStructureContent.root);
        } else {
            walletStructureContent = Transition.integrate(contentPane, "/layouts/wallet_structure.fxml", this.getClass());
            walletStructureContent.setInvalidateEventListner(this);

        }

        invalidate();

    }

    @FXML
    void initialize() {
        Image im = new Image(
                getClass().getResource("/assets/human.png").toString(), false);
        circleFrame.setImage(im);
        circleFrame.setFitHeight(60);

        try {
            walletStructureContent = Transition.integrate(contentPane, "/layouts/wallet_structure.fxml", this.getClass());
            walletStructureContent.setInvalidateEventListner(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DBServer database = new DBServer();
        bankAccounts = database.new BankAccounts();
        users = database.new Users();
    }


    @Override
    public void invalidate() {
        Node contentLayout = contentPane.getChildren().get(0);
        ArrayList<BankAccount> bankAccountArrayList = bankAccounts.select(user);
        if (walletStructureContent != null && contentLayout == walletStructureContent.root) {
            walletStructureContent.fillInInformation(bankAccountArrayList);
            walletStructureContent.setUser(user);
        } else if (rechargeFragmentContent != null && contentLayout == rechargeFragmentContent.root) {
            rechargeFragmentContent.fillInInformation(bankAccountArrayList);
        } else if (withdrawalFragmentContext != null && contentLayout == withdrawalFragmentContext.root) {
            withdrawalFragmentContext.fillInInformation(bankAccountArrayList);
        } else if (expensesLogFragmentContent != null && contentLayout == expensesLogFragmentContent.root) {
            expensesLogFragmentContent.fillInInformation(user);
        }
    }

    public void onClickWithdrawalBt(ActionEvent actionEvent) throws IOException {
        clearPane();
        resetSelectBt();
        withdrawalBt.getStyleClass().add("button-selected");

        if (withdrawalFragmentContext != null)
            Transition.integrate(contentPane, withdrawalFragmentContext.root);
        else {
            withdrawalFragmentContext = Transition.integrate(contentPane, "/layouts/withdrawal_fragment.fxml", this.getClass());
            withdrawalFragmentContext.setInvalidateEventListner(this);
        }

        invalidate();
    }

    public void onClickExpenseLogBt(ActionEvent actionEvent) throws IOException {

        clearPane();
        resetSelectBt();
        expenseLogBt.getStyleClass().add("button-selected");

        if (expensesLogFragmentContent != null)
            Transition.integrate(contentPane, expensesLogFragmentContent.root);
        else {
            expensesLogFragmentContent = Transition.integrate(contentPane, "/layouts/expense_log.fxml", this.getClass());
        }

        invalidate();
    }

    public void onMouseCkickedChangeUser(MouseEvent mouseEvent) throws IOException {
        URL logInURL = getClass().getResource("/layouts/sample.fxml");
        Transition.hideWindow(changeUser);
        Transition.openWindow(logInURL, false);
    }

    public void onMouseClickDelUser(MouseEvent mouseEvent) throws IOException {
        if (user != null) {
            users.delete(user);
            this.onMouseCkickedChangeUser(mouseEvent);
        }
    }
}

