package com.lordscave.ranchshopsimulator.controller;

import com.lordscave.ranchshopsimulator.utils.MessageType;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class MessageController {
    @FXML public Label lablemess;

    public  void setMes(MessageType type, String message) {
        if (type == MessageType.SUCCESS) {
            lablemess.setText("✅ " + message);
            lablemess.setStyle("-fx-text-fill: #3FF713;");
        } else if (type == MessageType.ERROR) {
            lablemess.setText("❌ " + message);
            lablemess.setStyle("-fx-text-fill: #F7090D;");
        }
    }
    public void showCustomMessage(MessageType type, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lordscave/ranchshopsimulator/components/Massage.fxml"));
            Parent root = loader.load();

            MessageController controller = loader.getController();
            controller.setMes(type, message);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(e -> stage.close());
            delay.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
