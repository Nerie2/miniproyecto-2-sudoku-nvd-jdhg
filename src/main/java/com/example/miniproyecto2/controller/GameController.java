package com.example.miniproyecto2.controller;

import com.example.miniproyecto2.model.Sudoku;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Objects;


/**
 * JavaFX controller for the main game view.
 *
 * <p>It manages the board UI events (cell selection and keyboard input), updates the grid from the
 * {@link com.example.miniproyecto2.model.Sudoku} model, and handles the start screen (start image + play button)
 * and the in-game hint button.</p>
 */
public class GameController {

    public GridPane gridPanel;
    int[] position = new int[2];
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView imgInicio;
    @FXML
    private Button btnJugar;
    @FXML
    private Button btnPista;
    Boolean focus = false;
    Boolean init = true;
    private int selectedNumber = 0;

    private static final List<String> CELL_STATE_CLASSES = List.of(
            "cell-fixed",
            "cell-user",
            "cell-hint",
            "cell-line-highlight",
            "cell-invalid-candidate",
            "cell-selected"
    );

    @FXML
    public void initialize() {
        // Estilo base de cada botón del tablero: blanco con borde gris (vía CSS).
        if (gridPanel != null) {
            for (Node node : gridPanel.getChildren()) {
                if (node instanceof Button b) {
                    if (!b.getStyleClass().contains("sudoku-cell")) {
                        b.getStyleClass().add("sudoku-cell");
                    }
                }
            }
            gridPanel.setVisible(false);
            gridPanel.setManaged(false);
        }

        if (btnPista != null) {
            btnPista.setVisible(false);
            btnPista.setManaged(false);
        }
    }
    public Button getButtonAt(int col, int row) {

        for (Node node : gridPanel.getChildren()) {

            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);


            int colIndex;

            colIndex = Objects.requireNonNullElse(c, 0);

            int rowIndex;
            rowIndex = Objects.requireNonNullElse(r, 0);

            if (colIndex == col && rowIndex == row) {
                return (Button) node;
            }
        }

        return null;
    }
    public void buttonAction(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        position = getButtonData(button);
        System.out.println(position[0] + ", " + position[1]);
        focus = true;

        int value = Sudoku.getInstance().infoGrid(position[0], position[1]);
        selectedNumber = value;
        applySelectionHighlights();
    }
    public int[] getButtonData(javafx.scene.control.Button button){
        try {
            String data = button.getUserData().toString();
            String[] parts = data.split(",");

            return new int[]{
                    Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim())
            };

        } catch (Exception e) {
            return new int[]{0, 0};
        }
    }

    Boolean checkInput(char letter){
        char[] numbers = {'1', '2', '3', '4', '5', '6'};
        for (char number : numbers) {
            if (number == letter) {
                return true;
            }
        }
        return false;

    }
    void sendUserInput(char letter){
        if(checkInput(letter)){
            if(Sudoku.getInstance().sendInput(position[0] ,position[1], letter-'0' )){
                focus = false;
                selectedNumber = letter - '0';
                refreshGrid();
                applySelectionHighlights();
            }
            else{
                System.out.println(" NO SE COLOCO");
            }
        }



    }
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        String input = event.getText();
        try  {
            if(focus){
                sendUserInput(input.charAt(0));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    void hint(){
        int[] hint = Sudoku.getInstance().searchInput();
        if (hint!=null){
            String text = String.valueOf(hint[2]);
            Button b = getButtonAt(hint[0], hint[1]);
            if (b != null) {
                b.setText(text);
                clearCellStateClasses(b);
                if (!b.getStyleClass().contains("sudoku-cell")) {
                    b.getStyleClass().add("sudoku-cell");
                }
                b.getStyleClass().addAll("cell-user", "cell-hint");
            }
        }

    }


 private void refreshGrid(){
     for (int i = 0; i <= 5; i++) {
         for (int j = 0; j <= 5; j++) {
             Button button = getButtonAt(i, j);
             if (button == null) continue;

             int value = Sudoku.getInstance().infoGrid(i, j);
             button.setText(value == 0 ? "" : String.valueOf(value));

             clearCellStateClasses(button);
             if (!button.getStyleClass().contains("sudoku-cell")) {
                 button.getStyleClass().add("sudoku-cell");
             }

             if (value != 0) {
                 if (Boolean.TRUE.equals(Sudoku.getInstance().checkOriginalNum(i, j))) {
                     button.getStyleClass().add("cell-fixed");
                 } else {
                     button.getStyleClass().add("cell-user");
                 }
             }
         }
     }
 }

    private void clearCellStateClasses(Button b) {
        b.getStyleClass().removeAll(CELL_STATE_CLASSES);
    }

    private void applySelectionHighlights() {
        if (gridPanel == null) return;

        // Limpia solo highlights dinámicos.
        for (Node node : gridPanel.getChildren()) {
            if (node instanceof Button b) {
                b.getStyleClass().removeAll(
                        "cell-line-highlight",
                        "cell-invalid-candidate",
                        "cell-selected"
                );
            }
        }

        if (selectedNumber <= 0) return;

        int selectedCol = position[0];
        int selectedRow = position[1];

        Button selectedButton = getButtonAt(selectedCol, selectedRow);
        if (selectedButton != null) {
            selectedButton.getStyleClass().add("cell-selected");
        }

        // Pinta fila y columna.
        for (int c = 0; c <= 5; c++) {
            Button b = getButtonAt(c, selectedRow);
            if (b != null) b.getStyleClass().add("cell-line-highlight");
        }
        for (int r = 0; r <= 5; r++) {
            Button b = getButtonAt(selectedCol, r);
            if (b != null) b.getStyleClass().add("cell-line-highlight");
        }

        // En esa fila/columna marca dónde NO puede ir el número seleccionado (solo vacías).
        for (int c = 0; c <= 5; c++) {
            markInvalidCandidateIfNeeded(c, selectedRow, selectedNumber);
        }
        for (int r = 0; r <= 5; r++) {
            markInvalidCandidateIfNeeded(selectedCol, r, selectedNumber);
        }
    }

    private void markInvalidCandidateIfNeeded(int col, int row, int number) {
        int value = Sudoku.getInstance().infoGrid(col, row);
        if (value != 0) return;
        if (Boolean.TRUE.equals(Sudoku.getInstance().checkOriginalNum(col, row))) return;

        boolean ok = Sudoku.getInstance().checkInput(col, row, number);
        if (!ok) {
            Button b = getButtonAt(col, row);
            if (b != null) b.getStyleClass().add("cell-invalid-candidate");
        }
    }

    @FXML
    private void onPlayClick() {
        if (imgInicio != null) {
            imgInicio.setVisible(false);
            imgInicio.setManaged(false);
        }
        if (btnJugar != null) {
            btnJugar.setVisible(false);
            btnJugar.setManaged(false);
        }
        if (gridPanel != null) {
            gridPanel.setManaged(true);
            gridPanel.setVisible(true);
        }
        if (btnPista != null) {
            btnPista.setManaged(true);
            btnPista.setVisible(true);
        }

        // Inicia la lógica del juego
        if(rootPane!=null){
            rootPane.requestFocus();
        }
        refreshGrid();
        init = false;
        applySelectionHighlights();
    }

    @FXML
    private void onPistaClick() {
        hint();
        applySelectionHighlights();
    }



}
