module com.example.sportingclub {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sportingclub to javafx.fxml;
    exports com.example.sportingclub;
}