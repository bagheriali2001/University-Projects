package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SceneController {

    public TextField widthTextField;
    public TextField heightTextField;
    public Button setCells;
    public Button populate;
    public Button randomPopulate;
    public Button start;
    public Button goOneStep;
    public Button restart;
    public GridPane cellsGrid;

    Cell[][] cells;
    Random random = new Random();
    Timer timer = new Timer();

    @FXML
    public void handleButtonAction(ActionEvent event) {
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));

        switch (eventName) {
            case "Set Cells":
                setCells();
                break;
            case "Populate":
                populate();
                break;
            case "Done":
                finishPopulate();
                break;
            case "Random Populate":
                randomPopulate();
                break;
            case "Start":
                start();
                break;
            case "End":
                end();
                break;
            case "Go One Step":
                nextGeneration();
                break;
            case "Restart":
                restart();
                break;
        }
    }

    private void setCells() {
        int tableWidth = Integer.parseInt(widthTextField.getText());
        int tableHeight = Integer.parseInt(heightTextField.getText());
        cells = new Cell[tableWidth][tableHeight];
        for (int i = 0; i < tableWidth; i++) {
            cellsGrid.addColumn(1);
            for (int j = 0; j < tableHeight; j++) {
                if (i == 0) {
                    cellsGrid.addRow(1);
                }
                Cell cell = new Cell();
                cell.setMinWidth(20);
                cell.setPrefWidth(20);
                cell.setMaxWidth(20);
                cell.setMinHeight(20);
                cell.setPrefHeight(20);
                cell.setMaxHeight(20);
                cell.setVisible(true);
                int temp = (i + 1) * 100 + (j + 1);
                cell.setId(temp + "");
                cells[i][j] = cell;
                cellsGrid.add(cells[i][j], i, j);
            }
        }
        setCells.setDisable(true);
    }

    private void populate() {
        for (int i = 0; i < Integer.parseInt(widthTextField.getText()); i++) {
            for (int j = 0; j < Integer.parseInt(heightTextField.getText()); j++) {
                cells[i][j].setOnAction(this::mouseClikedOnTiles);
            }
        }
        randomPopulate.setDisable(true);
        start.setDisable(true);
        goOneStep.setDisable(true);
        restart.setDisable(true);
        populate.setText("Done");
    }

    private void finishPopulate() {
        for (int i = 0; i < Integer.parseInt(widthTextField.getText()); i++) {
            for (int j = 0; j < Integer.parseInt(heightTextField.getText()); j++) {
                cells[i][j].setOnAction(null);
            }
        }
        randomPopulate.setDisable(false);
        start.setDisable(false);
        goOneStep.setDisable(false);
        restart.setDisable(false);
        populate.setText("Populate");
    }

    private void randomPopulate() {
        for (int i = 0; i < Integer.parseInt(widthTextField.getText()); i++) {
            for (int j = 0; j < Integer.parseInt(heightTextField.getText()); j++) {
                if (random.nextBoolean()) {
                    cells[i][j].full();
                }
            }
        }
    }

    private void start() {
        Timer newTimer = new Timer();
        newTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                nextGeneration();
            }
        }, 0, 250);
        populate.setDisable(true);
        randomPopulate.setDisable(true);
        goOneStep.setDisable(true);
        restart.setDisable(true);
        start.setText("End");
        timer=newTimer;
    }

    private void end() {
        timer.cancel();
        populate.setDisable(false);
        randomPopulate.setDisable(false);
        goOneStep.setDisable(false);
        restart.setDisable(false);
        start.setText("Start");
    }

    private void restart() {
        cellsGrid.getChildren().removeAll();
        cellsGrid.getColumnConstraints().removeAll();
        cellsGrid.getRowConstraints().removeAll();
        cellsGrid.getChildren().clear();
        setCells.setDisable(false);
    }

    private void mouseClikedOnTiles(ActionEvent event) {
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("id=") + 3);
        eventName = eventName.substring(0, eventName.indexOf(", styleClass"));
        int eventId = Integer.parseInt(eventName);
        int eventI = ((eventId - (eventId % 100)) / 100) - 1;
        int eventJ = (eventId % 100) - 1;
        if (cells[eventI][eventJ].isFull()) {
            cells[eventI][eventJ].remove();
        } else {
            cells[eventI][eventJ].full();
        }
    }

    private void nextGeneration() {
        int M = Integer.parseInt(widthTextField.getText());
        int N = Integer.parseInt(heightTextField.getText());

        boolean[][] futurecells = new boolean[M][N];

        // Loop through every cell
        for (int i = 1; i < M - 1; i++) {
            for (int j = 1; j < N - 1; j++) {
                int aliveNeighbours = 0;
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        aliveNeighbours += cells[i + x][j + y].getIsFull();
                    }
                }
                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                aliveNeighbours -= cells[i][j].getIsFull();
                // Implementing the Rules of Life
                if ((cells[i][j].getIsFull() == 1) && (aliveNeighbours < 2)) {
                    // Cell is lonely and dies
                    futurecells[i][j]=false;
                } else if ((cells[i][j].getIsFull() == 1) && (aliveNeighbours > 3)) {
                    // Cell dies due to over population
                    futurecells[i][j]=false;
                } else if ((cells[i][j].getIsFull() == 0) && (aliveNeighbours == 3)) {
                    // A new cell is born25
                    futurecells[i][j]=true;
                } else {
                    // Remains the same
                    futurecells[i][j] = cells[i][j].isFull();
                }
            }
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (futurecells[i][j]){
                    cells[i][j].full();
                }
                else {
                    cells[i][j].remove();
                }
            }
        }
    }
}
