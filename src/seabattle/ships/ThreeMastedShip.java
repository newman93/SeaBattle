package seabattle.ships;

import java.util.LinkedList;
import seabattle.cells.BattleCell;

public class ThreeMastedShip extends Ship {
    public ThreeMastedShip(int orientation, LinkedList<BattleCell> cells) {
        super(orientation, cells);
        this.armor = 3;
    }
}
