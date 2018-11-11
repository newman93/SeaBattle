package seabattle.board;

import java.util.LinkedList;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seabattle.cells.BattleCell;
import seabattle.cells.LabelCell;
import seabattle.ships.*;
import seabattle.status.Status;

public class Board extends Parent {
    private VBox rows = new VBox();
    private boolean enemy = false;
    private int ships = 10;
    private int shipsToPlace = 10;
    private int orientation = 0;
    private LinkedList<BattleCell> list = new LinkedList<BattleCell>();
    private Status status;
    public int gameStage = 0; //0 - placing ships, 1 - shotting. 3 - end of the game
    public Board opponentBoard;
    private Random random = new Random();
    
    public Board(boolean enemy, Status status, Board opponentBoard) {
        this.enemy = enemy;
        this.status = status;
        this.opponentBoard = opponentBoard;
        char alphabet = 'A';
        int number = 1;
        
        for (int y = 0; y <= 10; ++y){
            HBox row = new HBox();
            for (int x = 0; x <= 10; ++x) {
                if (y == 0) {
                    LabelCell l = new LabelCell((double)x,(double)y,this,String.valueOf(alphabet));
                    if (x == 0) {
                        row.getChildren().add(l);
                    } else { 
                        StackPane pane = new StackPane();
                        pane.setPrefSize(35,35);
                        pane.getChildren().addAll(l, l.getLabel());
                        row.getChildren().add(pane);
                        alphabet += 1;
                    }
                } else {
                    if (x == 0) {
                        LabelCell l = new LabelCell((double)x,(double)y,this,String.valueOf(number));
                        StackPane pane = new StackPane();
                        pane.setPrefSize(35,35);
                        pane.getChildren().addAll(l, l.getLabel());
                        row.getChildren().add(pane);
                        number += 1;
                    } else {
                        BattleCell b = new BattleCell((double)x,(double)y,this);
                        row.getChildren().add(b);
                        list.add(b);
                    }
                }   
            }
            rows.getChildren().add(row); 
        }
        getChildren().add(rows);
    }
    
    public void setOpponent(Board opponentBoard) {
        this.opponentBoard = opponentBoard;
    }
    
    public void updateStatus(int size) {
        this.status.setSize(String.valueOf(size));
    }
    
    public LinkedList<BattleCell> getBattleCell() {
        return this.list;
    }
    
    public void removeShip() {
        this.ships -= 1;
    }
    
    public boolean getEnemy() {
        return this.enemy;
    }
    
    public void placeShip(Ship ship, LinkedList<BattleCell> battleCells) {
        if (!getEnemy()) {
            for (BattleCell cell : battleCells) {    
                cell.setFill(Color.WHITE);
            }
        }
        int i = 0;
        for (BattleCell boardCell : list) {
            for (BattleCell cell : battleCells) {
                if (boardCell.getX() == cell.getX() && boardCell.getY() == cell.getY()) {
                   boardCell.setShip(ship);
                    list.set(i, cell); 
                }
            }
            i += 1;
        }
    }
    
    public LinkedList<BattleCell> canPlaceShip(Ship ship, double x, double y) {
        int length = ship.getArmor();
        LinkedList<BattleCell> list = new LinkedList<BattleCell>();
        
        if (ship.getOrientation() == 0) {
            canPlaceShip: {
                for (double i = y; i < y + length; ++i ) {
                    if (!isValidPoint(x,i)) {
                        list = null;
                        break;
                    }

                    BattleCell battleCell = getBattleCell(x,i);
                    if (battleCell.getShip() != null) {
                        list = null;
                        break;
                    }

                    for (BattleCell neighbor : getNeighbors((int)x, (int)i)) {
                        if (!isValidPoint(x, i)) {
                            list = null;
                            break canPlaceShip;
                        }

                        if (neighbor.getShip() != null) {
                            list = null;
                            break canPlaceShip;
                        }
                    }
                    list.add(battleCell);
                }
            }
        } else {
            canPlaceShip: {
                for (double i = x; i < x + length; i++) {
                    if (!isValidPoint(i, y)) {
                        list = null;
                        break;
                    }

                    BattleCell battleCell = getBattleCell(i, y);
                    if (battleCell.getShip() != null) {
                        list = null;
                        break;
                    }

                    for (BattleCell neighbor : getNeighbors((int)i, (int)y)) {
                        if (!isValidPoint(i, y)) {
                            list = null;
                            break canPlaceShip;
                        }

                        if (neighbor.getShip() != null) {
                            list = null;
                            break canPlaceShip;
                        }
                    }
                    list.add(battleCell);
                }
            }
        }
        
        return list;
    }
    
    private BattleCell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x- 1, y - 1),
                new Point2D(x + 1, y - 1),    
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1),
                new Point2D(x- 1, y + 1),
                new Point2D(x + 1, y + 1)
        };

        LinkedList<BattleCell> neighbors = new LinkedList<BattleCell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getBattleCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new BattleCell[0]);
    }
    
    private boolean isValidPoint(double x, double y) {
        return x > 0 && x<= 10 && y > 0 && y <= 10;
    }
   
    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }
    
    public int getShipsToPlace() {
        return shipsToPlace;
    }
    
    public int getOrientation() {
        return orientation;
    }
    
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    
    public BattleCell getBattleCell(double x, double y) {
        return (BattleCell)((HBox)rows.getChildren().get((int)y)).getChildren().get((int)x);
    }   
    
    public void removeShipToPlace() {
        this.shipsToPlace -= 1;
    }
    
    public int getShips() {
        return this.ships;
    }
    
    public boolean fireComputer() {
        while (true) {
            int x = random.nextInt(10 - 1 + 1) + 1;
            int y = random.nextInt(10 - 1 + 1) + 1;

            BattleCell cell = getBattleCell(x, y);
            if (cell.fired == false) {
                boolean fire = cell.fire();
                break;
            }
        }
        return true;
    }    
    
    public void setComputerShips() {
        if (!getEnemy()) {
            LinkedList<BattleCell> ship4_1_list = new LinkedList<BattleCell>();
            ship4_1_list.add(opponentBoard.getBattleCell(1.0,1.0));
            ship4_1_list.add(opponentBoard.getBattleCell(2.0,1.0));
            ship4_1_list.add(opponentBoard.getBattleCell(3.0,1.0));
            ship4_1_list.add(opponentBoard.getBattleCell(4.0,1.0));
            FourMastedShip ship4_1 = new FourMastedShip(1, ship4_1_list);
            opponentBoard.placeShip(ship4_1, ship4_1.getCells());
            
            LinkedList<BattleCell> ship3_1_list = new LinkedList<BattleCell>();
            ship3_1_list.add(opponentBoard.getBattleCell(1.0,3.0));
            ship3_1_list.add(opponentBoard.getBattleCell(1.0,4.0));
            ship3_1_list.add(opponentBoard.getBattleCell(1.0,5.0));
            ThreeMastedShip ship3_1 = new ThreeMastedShip(0, ship3_1_list);
            opponentBoard.placeShip(ship3_1, ship3_1.getCells());
            
            LinkedList<BattleCell> ship3_2_list = new LinkedList<BattleCell>();
            ship3_2_list.add(opponentBoard.getBattleCell(8.0,10.0));
            ship3_2_list.add(opponentBoard.getBattleCell(9.0,10.0));
            ship3_2_list.add(opponentBoard.getBattleCell(10.0,10.0));
            ThreeMastedShip ship3_2 = new ThreeMastedShip(1, ship3_2_list);
            opponentBoard.placeShip(ship3_2, ship3_2.getCells());
            
            LinkedList<BattleCell> ship2_1_list = new LinkedList<BattleCell>();
            ship2_1_list.add(opponentBoard.getBattleCell(4.0,4.0));
            ship2_1_list.add(opponentBoard.getBattleCell(5.0,4.0));
            TwoMastedShip ship2_1 = new TwoMastedShip(1, ship2_1_list);
            opponentBoard.placeShip(ship2_1, ship2_1.getCells());
            
            LinkedList<BattleCell> ship2_2_list = new LinkedList<BattleCell>();
            ship2_2_list.add(opponentBoard.getBattleCell(4.0,6.0));
            ship2_2_list.add(opponentBoard.getBattleCell(4.0,7.0));
            TwoMastedShip ship2_2 = new TwoMastedShip(0, ship2_2_list);
            opponentBoard.placeShip(ship2_2, ship2_2.getCells());
            
            LinkedList<BattleCell> ship2_3_list = new LinkedList<BattleCell>();
            ship2_3_list.add(opponentBoard.getBattleCell(8.0,7.0));
            ship2_3_list.add(opponentBoard.getBattleCell(8.0,8.0));
            TwoMastedShip ship2_3 = new TwoMastedShip(1, ship2_3_list);
            opponentBoard.placeShip(ship2_3, ship2_3.getCells());
            
            LinkedList<BattleCell> ship1_1_list = new LinkedList<BattleCell>();
            ship1_1_list.add(opponentBoard.getBattleCell(2.0,9.0));
            OneMastedShip ship1_1 = new OneMastedShip(0, ship1_1_list);
            opponentBoard.placeShip(ship1_1, ship1_1.getCells());
            
            LinkedList<BattleCell> ship1_2_list = new LinkedList<BattleCell>();
            ship1_2_list.add(opponentBoard.getBattleCell(6.0,8.0));
            OneMastedShip ship1_2 = new OneMastedShip(0, ship1_2_list);
            opponentBoard.placeShip(ship1_2, ship1_2.getCells());
            
            LinkedList<BattleCell> ship1_3_list = new LinkedList<BattleCell>();
            ship1_3_list.add(opponentBoard.getBattleCell(8.0,3.0));
            OneMastedShip ship1_3 = new OneMastedShip(0, ship1_3_list);
            opponentBoard.placeShip(ship1_3, ship1_3.getCells());

            LinkedList<BattleCell> ship1_4_list = new LinkedList<BattleCell>();
            ship1_4_list.add(opponentBoard.getBattleCell(10.0,4.0));
            OneMastedShip ship1_4 = new OneMastedShip(0, ship1_4_list);
            opponentBoard.placeShip(ship1_4, ship1_4.getCells());
            shipsToPlace = 0;
        }
    }
}
