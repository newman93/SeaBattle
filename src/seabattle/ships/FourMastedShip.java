package seabattle.ships;

import java.util.LinkedList;
import seabattle.cells.BattleCell;

public class FourMastedShip extends Ship {
    public FourMastedShip(int orientation,LinkedList<BattleCell> cells) {
        super(orientation, cells);
        this.armor = 4;
    }
}
