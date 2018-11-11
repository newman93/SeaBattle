package seabattle.status;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Status extends Parent {
    private VBox rows = new VBox();
    private StringProperty size = new SimpleStringProperty("4");
    private StringProperty orientation = new SimpleStringProperty("Vertical");
    
    public Status() {
        rows.setPrefWidth(200);
        rows.getChildren().add(new Label("Status:"));
        
        HBox row2 = new HBox();
        row2.getChildren().add(new Label("Ship size: "));
        Label label1 = new Label();
        label1.textProperty().bind(size);
        row2.getChildren().add(label1);
        rows.getChildren().add(row2);
        
        HBox row3 = new HBox();
        row3.getChildren().add(new Label("Ship orientation: "));
        Label label2 = new Label();
        label2.textProperty().bind(orientation);
        row3.getChildren().add(label2);
        rows.getChildren().add(row3);
        
        getChildren().add(rows);
    }
    
    public void setSize(String size) {
        this.size.setValue(size);
    }
    
    public void setOrientation(int orientation) {
        switch (orientation) {
            case 0: 
                    this.orientation.setValue("Vertical");
                    break;
            case 1:
                    this.orientation.setValue("Horizontal");
                    break;
            default:
                    this.orientation.setValue("Vertical");
                    break;
        }
    }
}
