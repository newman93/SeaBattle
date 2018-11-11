package seabattle;

import java.util.logging.Level;
import java.util.logging.Logger;
import seabattle.board.Board;
import seabattle.status.Status;
import seabattle.ships.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SeaBattleMain extends Application {
    private Board enemyBoard, playerBoard;
    private Status status;
    
    private Parent createContent(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1200, 600);
        
       MenuBar menuBar = new MenuBar();
       Menu newGameMenu = new Menu("Menu");
       MenuItem newGameMenuItem = new MenuItem("New game");
       newGameMenuItem.setOnAction(actionEvent -> {
           primaryStage.close();
           Platform.runLater( () -> {
               try {
                   new SeaBattleMain().start( new Stage() );
               } catch (Exception ex) {
                   Logger.getLogger(SeaBattleMain.class.getName()).log(Level.SEVERE, null, ex);
               }
           } );
        });
        
        newGameMenu.getItems().add(newGameMenuItem);
        menuBar.getMenus().add(newGameMenu);
        root.setTop(menuBar);
        
        status = new Status();
        
        enemyBoard = new Board(true, status, null);
        playerBoard = new Board(false, status, null);
        enemyBoard.setOpponent(playerBoard);
        playerBoard.setOpponent(enemyBoard);
        
        HBox hbox = new HBox(50, playerBoard, status, enemyBoard);
        hbox.setAlignment(Pos.CENTER);

        root.setCenter(hbox);
        
        
        
        return root;
    }
    
    EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.SECONDARY) {
                if (playerBoard.getOrientation() == 0) {
                    playerBoard.setOrientation(1);
                    status.setOrientation(1);
                } else {
                   playerBoard.setOrientation(0); 
                   status.setOrientation(0);                }
            }
        }    
    };
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent(primaryStage));
        scene.setOnMouseClicked(mouseClicked);
        primaryStage.setTitle("SeaBattle");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }   
}
