module com.mypadelapp.padelapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mypadelapp.padelapp to javafx.fxml;
    exports com.mypadelapp.padelapp;
}
