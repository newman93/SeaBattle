package seabattle.board;

import java.util.LinkedList;
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
    
    public Board(boolean enemy, Status status) {
        this.enemy = enemy;
        this.status = status;
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
        for (BattleCell cell : battleCells) {
                cell.setFill(Color.WHITE);
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
            for (double i = y; i < y + length; ++i ) {
                if (!isValidPoint(x,i)) {
                    return null;
                }
                
                BattleCell battleCell = getBattleCell(x,i);
                if (battleCell.getShip() != null) {
                    return null;
                }
                
                for (BattleCell neighbor : getNeighbors((int)x, (int)i)) {
                    if (!isValidPoint(x, i))
                        return null;

                    if (neighbor.getShip() != null)
                        return null;
                }
                list.add(battleCell);
            }
        } else {
            for (double i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return null;

                BattleCell battleCell = getBattleCell(i, y);
                if (battleCell.getShip() != null)
                    return null;

                for (BattleCell neighbor : getNeighbors((int)i, (int)y)) {
                    if (!isValidPoint(i, y))
                        return null;

                    if (neighbor.getShip() != null)
                        return null;
                }
                list.add(battleCell);
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
}
