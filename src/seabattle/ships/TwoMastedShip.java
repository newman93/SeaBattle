package seabattle.ships;

import java.util.LinkedList;
import seabattle.cells.BattleCell;

public class TwoMastedShip extends Ship {
    public TwoMastedShip(int orientation, LinkedList<BattleCell> cells) {
        super(orientation, cells);
        this.armor = 2;
    }
}
