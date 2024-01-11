module com.example.pingpong {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.aztecchallenge to javafx.fxml;
    exports com.example.aztecchallenge;
}