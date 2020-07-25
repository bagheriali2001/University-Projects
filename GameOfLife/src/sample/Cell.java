package sample;

import javafx.scene.control.Button;

public class Cell extends Button {
    private boolean isFull = false;

    public Cell() {
        this.setStyle("-fx-background-color: #000000; -fx-background-radius : 0; -fx-border-radius: 0;");
    }

    public void full(){
        isFull=true;
        this.setStyle(null);
        this.setStyle("-fx-background-color: #fff900; -fx-background-radius : 0; -fx-border-radius: 0;");
    }

    public void remove(){
        isFull=false;
        this.setStyle(null);
        this.setStyle("-fx-background-color: #000000; -fx-background-radius : 0; -fx-border-radius: 0;");
    }

    public int getIsFull() {
        if (isFull){
            return 1;
        }
        else {
            return 0;
        }
    }

    public boolean isFull() {
        return isFull;
    }
}
