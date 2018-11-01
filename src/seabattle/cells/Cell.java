package seabattle.cells;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import seabattle.board.Board;

public class Cell extends Rectangle {
    Board board;
    
    public Cell(double x, double y, Board board) {
        super(x,y,35,35);
        this.board = board;
        setFill(Color.TRANSPARENT);
    }
}
