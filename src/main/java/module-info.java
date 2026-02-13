module com.mypadelapp.padelapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mypadelapp.padelapp to javafx.fxml;
    exports com.mypadelapp.padelapp;
}
