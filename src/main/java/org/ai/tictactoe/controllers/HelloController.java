package org.ai.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ai.tictactoe.HelloApplication;
import org.ai.tictactoe.logic.game.GameType;

import java.io.IOException;

public class HelloController {
    @FXML
    protected void choosePlayerGame(ActionEvent event) throws IOException {
        GameController.gameType = GameType.PLAYER_VS_PLAYER;

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("Game.fxml")), 1140, 780);
        stage.setScene(scene);
    }

    @FXML
    protected void chooseAIGame(ActionEvent event) throws IOException {
        GameController.gameType = GameType.PLAYER_VS_AI;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("ComputerLevel.fxml")), 1140, 780);
        stage.setScene(scene);
    }

    public void chooseComputerVsComputerGame(ActionEvent event) throws IOException {
        GameController.gameType = GameType.AI_VS_AI;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(HelloApplication.class.getResource("ComputerLevel.fxml")), 1140, 780);
        stage.setScene(scene);
    }
}