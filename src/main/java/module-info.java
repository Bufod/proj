module proj.main {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.java;

    opens code to javafx.fxml;
    exports code;

    opens code.controllers to javafx.fxml;
    exports code.controllers;

    exports code.entity;

}