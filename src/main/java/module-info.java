module org.ai.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.ai.tictactoe to javafx.fxml;
    exports org.ai.tictactoe;
}