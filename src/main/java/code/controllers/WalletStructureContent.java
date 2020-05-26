package code.controllers;

import code.*;
import code.entity.BankAccount;
import code.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class WalletStructureContent {
    public VBox verticalBox;
    public Text totalBalances;
    public AnchorPane root;
    public Button delBankAccount;
    @FXML
    private StackPane chartPane;
    public ArrayList<Color> colors;
    private DoughnutChart chart = null;
    private HashSet<Color> colorHashSet;
    @FXML
    ScrollPane scrollPane;

    private InvalidateEventListner listner;
    private User user;
    private ArrayList<BankAccount> bankAccountsList;
    private AddBankAccountController addBankAccountController;
    private DBServer.BankAccounts bankAccounts;

    public WalletStructureContent() {
        colors = new ArrayList<>();
        colorHashSet = new HashSet<>();
    }

    public void fillEnum(ArrayList<BankAccount> bankAccounts) {
        verticalBox.getChildren().clear();
        for (int i = 0; i < bankAccounts.size(); i++) {
            BankAccount account = bankAccounts.get(i);
            Node node = null;
            try {
                node = FXMLLoader.load(getClass().getResource("/layouts/item_enum.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (node != null) {
                Pane pane = (Pane) node;
                pane.getStylesheets().add("/style/pane.css");
                pane.getStyleClass().add("element");
                Text accountName = (Text) node.lookup("#accountName"),
                        balances = (Text) node.lookup("#balances");

                accountName.setText(account.getAccountName());
                balances.setText(String.valueOf(account.getBalances()));
                pane.setOnMouseClicked((e) -> {
                    pane.requestFocus();
                });
                verticalBox.getChildren().add(node);
            }
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setInvalidateEventListner(InvalidateEventListner listner) {
        this.listner = listner;
    }

    public void fillChartFromList(ArrayList<BankAccount> bankAccounts) {
        ObservableList<PieChart.Data> pieChartData = createData(bankAccounts);

        chartPane.getChildren().clear();
        chart = new DoughnutChart(pieChartData);
        chart.setTitle("Распределение баланса");
        chartPane.getChildren().add(chart);
    }

    float getTotalBalances(ArrayList<BankAccount> bankAccounts) {
        float total = 0;
        for (BankAccount acc :
                bankAccounts) {
            total += acc.getBalances();
        }
        return total;
    }

    void fillInInformation(ArrayList<BankAccount> bankAccounts) {
        this.bankAccountsList = bankAccounts;
        fillChartFromList(bankAccounts);
        fillEnum(bankAccounts);
        totalBalances.setText(String.valueOf(getTotalBalances(bankAccounts)) + " \u20BD");
    }

    @FXML
    void addBankAccountOnClick(ActionEvent event) throws IOException {
        chartPane.getChildren().clear();
        if (addBankAccountController != null) {
            Transition.integrate(chartPane, addBankAccountController.root);
        } else {
            addBankAccountController = Transition.integrate(chartPane,
                    "/layouts/add_bank_account.fxml", this.getClass());
            addBankAccountController.setInvalidateEventListner(listner);
            addBankAccountController.setUser(user);
        }
    }

    private ObservableList<PieChart.Data> createData(ArrayList<BankAccount> bankAccounts) {
        ObservableList<PieChart.Data> observableList = FXCollections.observableArrayList();
        final Label caption = new Label("");
        caption.setTextFill(Color.WHITE);
        caption.setStyle("-fx-font: 12 arial;");

        for (BankAccount bankAccount : bankAccounts) {
            PieChart.Data data = new PieChart.Data(bankAccount.getAccountName(), bankAccount.getBalances());
            observableList.add(data);
        }
        return observableList;
    }

    public void delBankAccountOnClick(ActionEvent actionEvent) {
        ObservableList<Node> nodes = verticalBox.getChildren();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).isFocused())
                bankAccounts.delete(bankAccountsList.get(i));
        }
        listner.invalidate();
    }

    @FXML
    void initialize() {
        DBServer database = new DBServer();
        bankAccounts = database.new BankAccounts();
        delBankAccount.setFocusTraversable(false);
    }

}
