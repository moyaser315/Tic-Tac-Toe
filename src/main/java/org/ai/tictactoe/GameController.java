package org.ai.tictactoe;

import javafx.event.ActionEvent;
import org.ai.tictactoe.player.GameDifficulty;

public class GameController {
    public static GameDifficulty difficultyLevel;


    public void show(ActionEvent event) {
        System.out.println(difficultyLevel);
    }
}
