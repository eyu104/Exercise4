module com.zy.exercise3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.zy.exercise3 to javafx.fxml;
    opens com.zy.exercise3.Controller to javafx.fxml;
    opens img to javafx.fxml;
    exports com.zy.exercise3;
    exports com.zy.exercise3.Controller;

}