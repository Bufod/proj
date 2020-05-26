package code;

import javafx.scene.control.TextField;

public abstract class Verificator {

    //проверка заполнености
    public static boolean checkFillTextField(TextField[] tfs) {

        boolean ok = true;
        for (TextField tf :
                tfs) {
            if (tf != null && tf.getText().equals("")) {
                tf.setStyle("-fx-border-color:red;");
                ok = false;
            } else if (tf != null)
                tf.setStyle("-fx-border-color:green;");
        }
        return ok;
    }

    //проверка заполнености
    public static boolean checkFillTextField(TextField tf) {
        if (tf != null && tf.getText().equals("")) {
            tf.setStyle("-fx-border-color:red;");
            return false;
        } else if (tf != null)
            tf.setStyle("-fx-border-color:green;");
        return true;
    }
}
