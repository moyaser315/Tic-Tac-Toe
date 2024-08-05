package org.ai.tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ai.tictactoe.player.GameDifficulty;

import java.io.IOException;

public class ComputerLevelController {
    public void chooseInsaneLevel(ActionEvent event) {
        GameController.difficultyLevel = GameDifficulty.INSANE;
        enterGame(event);
    }

    public void chooseHardLevel(ActionEvent event) {
        GameController.difficultyLevel = GameDifficulty.HARD;
        enterGame(event);
    }

    public void chooseMediumLevel(ActionEvent event) {
        GameController.difficultyLevel = GameDifficulty.MEDIUM;
        enterGame(event);
    }

    public void chooseEasyLevel(ActionEvent event) {
        GameController.difficultyLevel = GameDifficulty.EASY;
        enterGame(event);
    }

    private void enterGame(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("Game.fxml")), 1140, 780);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
    }
}
