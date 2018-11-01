package seabattle.cells;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import seabattle.board.Board;

public class LabelCell extends Cell {
    private Text label;
    
    public LabelCell(double x, double y, Board board, String label) {
        super(x,y,board);
        this.label = new Text(label); 
        setStroke(Color.BLACK);
        this.label.setStyle("-fx-font-weight: bold");
    }
    
    public Text getLabel() {
        return label;
    }
}
