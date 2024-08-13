package org.ai.tictactoe.logic.player;

import org.ai.tictactoe.logic.game.GameBoard;
import org.ai.tictactoe.logic.game.GameSymbol;

public class HumanPlayer extends Player {
    public HumanPlayer(GameSymbol sym) {
        super(sym);
    }

    @Override
    public void play(GameBoard b) {
        // Do nothing here
    }

    public void play(GameBoard b, int[] move) {
        b.place(s, move[0], move[1]);
    }
}

// int[] move = getMove(b);

// public int[] getMove(Board b) {
    //     int row = scanner.nextInt();
    //     int col = scanner.nextInt();
    
    //     while (!b.isValidPosition(row, col) || !b.isEmpty(row, col)) {
//         System.out.println("Invalid Position... Try Again");
//         row = scanner.nextInt();
//         col = scanner.nextInt();
//     }

//     return new int[] { row, col };
// }