module org.ai.tictagtoe {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.ai.tictactoe to javafx.fxml;
    exports org.ai.tictactoe;
    exports org.ai.tictactoe.controllers;
    opens org.ai.tictactoe.controllers to javafx.fxml;
}