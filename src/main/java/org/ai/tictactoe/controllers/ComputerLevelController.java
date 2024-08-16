package org.ai.tictactoe.controllers;

import java.io.IOException;

import org.ai.tictactoe.HelloApplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ComputerLevelController {
    private static final int EASY = 1;
    private static final int MEDIUM = 2;
    private static final int HARD = 3;
    private static final int INSANE = 5;

    public void chooseInsaneLevel(ActionEvent event) {
        GameController.difficultyLevel = INSANE;
        enterGame(event);
    }

    public void chooseHardLevel(ActionEvent event) {
        GameController.difficultyLevel = HARD;
        enterGame(event);
    }

    public void chooseMediumLevel(ActionEvent event) {
        GameController.difficultyLevel = MEDIUM;
        enterGame(event);
    }

    public void chooseEasyLevel(ActionEvent event) {
        GameController.difficultyLevel = EASY;
        enterGame(event);
    }

    private void enterGame(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("Game.fxml")), 1140, 780);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
    }
}
