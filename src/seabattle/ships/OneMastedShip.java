package seabattle.ships;

import java.util.LinkedList;
import seabattle.cells.BattleCell;

public class OneMastedShip extends Ship {
    public OneMastedShip(int orientation, LinkedList<BattleCell> cells) {
        super(orientation, cells);
        this.armor = 1;
    }
}
