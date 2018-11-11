package seabattle.cells;

import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import seabattle.board.Board;
import seabattle.ships.FourMastedShip;
import seabattle.ships.*;

public class BattleCell extends Cell {
    private Ship ship = null;
    public boolean fired = false;
    
    public BattleCell(double x, double y, Board board) {
        super(x, y, board);
        if (board.getEnemy()) {
            setFill(Color.LIGHTGREY);
        } else {
            setFill(Color.BLUE);
        }
        setStroke(Color.BLACK);
        this.setOnMouseMoved(mouseMoved);
        this.setOnMouseClicked(mouseClicked);
    }
    
    public boolean fire() {
        fired = true;

        if (ship != null) {
            ship.hit();
            setFill(Color.YELLOW);

            if (ship.isSunk()) {
                board.removeShip();
                ship.sankShip();
            }
            return true;
        } else {
            if (board.getEnemy()) {
                setFill(Color.BLUE);
            } else {
              setFill(Color.DARKGREY);
            }
            return false;
        }
    }
    
    public void sankShip() {
        setFill(Color.RED);
    }
    
    public Ship getShip() {
        return ship;
    }
    
    public void setShip(Ship ship) {
        this.ship = ship;
    }
    
    private void higlightCell(LinkedList<BattleCell> list) {
        for (BattleCell battleCell: list) {
            battleCell.setFill(Color.LIGHTGRAY);
        }
    }
    
    private void resetBoard() {
        for (BattleCell battleCell : board.getBattleCell()) {
            if (battleCell.getShip() == null) {
                battleCell.setFill(Color.BLUE);
            }
        }
    }
    
    private int checkType() {
        int type;
        int shipsToPlace = board.getShipsToPlace();
        if (shipsToPlace == 10) {
            type = 4;
        } else if (shipsToPlace >= 8 && shipsToPlace < 10) {
            type = 3;
        } else if (shipsToPlace >= 5 && shipsToPlace < 8) {
            type = 2;
        } else if (shipsToPlace >= 1 && shipsToPlace < 5) {
            type = 1;
        } else {
            type = 0;
        }
        
        return type;
    }
    
    EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!board.getEnemy()) {
                    if (board.gameStage == 0) {
                        resetBoard();
                        BattleCell battleCell = (BattleCell) event.getSource();

                        int type = checkType();
                            
                        switch (type) {
                            case 4: 
                                    FourMastedShip ship4 = new FourMastedShip(board.getOrientation(), null);
                                    if (board.canPlaceShip(ship4, battleCell.getX(), battleCell.getY()) != null) {
                                       ship4.setCells(board.canPlaceShip(ship4, battleCell.getX(), battleCell.getY()));
                                       board.placeShip(ship4, ship4.getCells());
                                       board.removeShipToPlace();
                                       board.updateStatus(checkType());
                                    }
                                    break;
                            case 3:
                                    ThreeMastedShip ship3 = new ThreeMastedShip(board.getOrientation(), null);
                                    if (board.canPlaceShip(ship3, battleCell.getX(), battleCell.getY()) != null) {
                                       ship3.setCells(board.canPlaceShip(ship3, battleCell.getX(), battleCell.getY()));
                                       board.placeShip(ship3, ship3.getCells());
                                       board.removeShipToPlace();
                                       board.updateStatus(checkType());
                                    }
                                    break;
                            case 2:
                                    TwoMastedShip ship2 = new TwoMastedShip(board.getOrientation(), null);
                                    if (board.canPlaceShip(ship2, battleCell.getX(), battleCell.getY()) != null) {
                                       ship2.setCells(board.canPlaceShip(ship2, battleCell.getX(), battleCell.getY()));
                                       board.placeShip(ship2, ship2.getCells());
                                       board.removeShipToPlace();
                                       board.updateStatus(checkType());
                                    }
                                    break;
                            case 1:
                                    OneMastedShip ship1 = new OneMastedShip(board.getOrientation(), null);
                                    if (board.canPlaceShip(ship1, battleCell.getX(), battleCell.getY()) != null) {
                                       ship1.setCells(board.canPlaceShip(ship1, battleCell.getX(), battleCell.getY()));
                                       board.placeShip(ship1, ship1.getCells());
                                       board.removeShipToPlace();
                                       board.updateStatus(checkType());
                                    }
                                    if (board.getShipsToPlace() < 1) {
                                        board.setComputerShips();
                                        board.gameStage = 1;
                                        board.opponentBoard.gameStage = 1;
                                    }
                                    break;
                            default:
                                    break;
                        }
                    }
                } else {
                    if (board.gameStage == 1) {
                        BattleCell battleCell = (BattleCell) event.getSource();
                        if (battleCell.fired == false) {
                            if (battleCell.fire() && board.getShips() == 0) {
                                board.gameStage = 3;
                                board.opponentBoard.gameStage = 3;
                                Alert alert = new Alert(AlertType.INFORMATION, "Congratulations! You won!", ButtonType.OK);
                                alert.setHeaderText("Game status");
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alert.show();
                            } else {
                                if (board.opponentBoard.fireComputer() && board.opponentBoard.getShips() == 0) {
                                    board.gameStage = 3;
                                    board.opponentBoard.gameStage = 3;
                                    Alert alert = new Alert(AlertType.INFORMATION, "You lost!", ButtonType.OK);
                                    alert.setHeaderText("Game status");
                                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                    alert.show();
                                }        
                            }
                        }
                    }
                }
            }
        }
    };
    
    EventHandler<MouseEvent> mouseMoved = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (!board.getEnemy()) {
                if (board.gameStage == 0) {
                    resetBoard();
                    BattleCell battleCell = (BattleCell) event.getSource();

                    int type = checkType();

                    switch (type) {
                        case 4: 
                                FourMastedShip ship4 = new FourMastedShip(board.getOrientation(), null);
                                if (board.canPlaceShip(ship4, battleCell.getX(), battleCell.getY()) != null) {
                                   higlightCell(board.canPlaceShip(ship4, battleCell.getX(), battleCell.getY()));         
                                }
                                break;
                        case 3:
                                ThreeMastedShip ship3 = new ThreeMastedShip(board.getOrientation(), null);
                                if (board.canPlaceShip(ship3, battleCell.getX(), battleCell.getY()) != null) {
                                   higlightCell(board.canPlaceShip(ship3, battleCell.getX(), battleCell.getY()));         
                                }
                                break;
                        case 2:
                                TwoMastedShip ship2 = new TwoMastedShip(board.getOrientation(), null);
                                if (board.canPlaceShip(ship2, battleCell.getX(), battleCell.getY()) != null) {
                                   higlightCell(board.canPlaceShip(ship2, battleCell.getX(), battleCell.getY()));         
                                }
                                break;
                        case 1:
                                OneMastedShip ship1 = new OneMastedShip(board.getOrientation(), null);
                                if (board.canPlaceShip(ship1, battleCell.getX(), battleCell.getY()) != null) {
                                   higlightCell(board.canPlaceShip(ship1, battleCell.getX(), battleCell.getY()));         
                                }
                                break;
                        default:
                                break;
                    }
                }
            }
        }
    };
}
