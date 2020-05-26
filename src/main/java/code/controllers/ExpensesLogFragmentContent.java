package code.controllers;

import code.DBServer;
import code.entity.Expense;
import code.entity.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

public class ExpensesLogFragmentContent {
    public TableView<Expense> table;
    public TableColumn<Expense, String> categoryCol;
    public TableColumn<Expense, Float> ammountCol;
    public TableColumn<Expense, String> accountCol;
    public TableColumn<Expense, String> descriptionCol;
    public AnchorPane root;
    private DBServer.Expenses expenses;

    void fillInInformation(User user) {
        categoryCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("category"));
        ammountCol.setCellValueFactory(new PropertyValueFactory<Expense, Float>("amount"));
        accountCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("accountName"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("description"));
        table.setItems(FXCollections.observableList(expenses.selectAll(user)));
    }

    @FXML
    void initialize() {
        DBServer database = new DBServer();
        expenses = database.new Expenses();
        table.setPlaceholder(new Label("Нет записей"));
    }

    public void onClickClearAllBt(ActionEvent actionEvent) {
        try {
            expenses.deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.getItems().clear();
    }
}
