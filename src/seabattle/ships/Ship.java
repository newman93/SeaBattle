package seabattle.ships;

import java.util.LinkedList;
import javafx.scene.Parent;
import seabattle.cells.BattleCell;
import seabattle.cells.Cell;

public class Ship extends Parent{
    private int orientation = 0; //0 - vertical 1 - horizontal 
    LinkedList<BattleCell> cells;
    int armor = 0;
    
    public Ship(int orientation, LinkedList<BattleCell> cells) {
        this.orientation = orientation;
        this.cells = cells;
    }
    
    public void hit() {
        armor -= 1;
    }
    
    public boolean isSunk() {
        return armor <= 0;
    }
    
    public void sankShip() {
        for (BattleCell battleCell : cells) {
            battleCell.sankShip();
        }
    }
    
    public int getArmor() {
        return armor;
    }
    
    public int getOrientation() {
        return orientation;
    }
    
    public void setCells(LinkedList<BattleCell> cells) {
        this.cells = cells;
    }
    
    public LinkedList<BattleCell> getCells() {
        return cells;
    }
}
