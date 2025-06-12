module com.lordscave.ranchshopsimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.lordscave.ranchshopsimulator to javafx.fxml;
    opens com.lordscave.ranchshopsimulator.controller to javafx.fxml;
    opens com.lordscave.ranchshopsimulator.model to com.fasterxml.jackson.databind;


    exports com.lordscave.ranchshopsimulator;
    exports com.lordscave.ranchshopsimulator.controller;

}