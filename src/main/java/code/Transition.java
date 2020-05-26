package code;

import code.entity.Bundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Transition {

    public static Pane integrate(Pane parentPane, Pane childPane) throws IOException {
        parentPane.getChildren().add(childPane);
        return parentPane;
    }

    public static <T> T integrate(Pane pane, URL pathToFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(pathToFxml);
        Parent rootLayout = loader.load();
        AnchorPane.setBottomAnchor(rootLayout, 0.0);
        AnchorPane.setTopAnchor(rootLayout, 0.0);
        AnchorPane.setLeftAnchor(rootLayout, 0.0);
        AnchorPane.setRightAnchor(rootLayout, 0.0);
        pane.getChildren().add(rootLayout);
        return loader.getController();
    }

    public static <T> T integrate(Pane pane, String pathToFxml, Class classController) throws IOException {
        return integrate(pane, classController.getResource(pathToFxml));
    }

    public static void setBundle(Bundle bundle, Receivable receivable){
        receivable.setBundle(bundle);
    }


    //открыть новое окно на основе пути к файлу разметки
    public static <T> T openWindow(URL pathToFxml, boolean minSizeRule) throws IOException {
        FXMLLoader loader = new FXMLLoader(pathToFxml);
        //путь к разметке, корневому узлу
        Parent rootLayout = loader.load();
        //новая площадка/арена/этап/стадия
        Stage stage = new Stage();
        //задать сцену
        stage.setScene(new Scene(rootLayout));
        if (minSizeRule) {
            stage.setMinWidth(800);
            stage.setMinHeight(400);
        }
        //показать немедленно
        stage.show();
        return loader.getController();
    }

    //закрыть окно в котором есть этот Node
    public static void hideWindow(Node node){
        //закрыть существующее окно
        node.getScene().getWindow().hide();
    }
}
