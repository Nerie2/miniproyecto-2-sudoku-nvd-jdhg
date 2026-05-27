package com.example.miniproyecto2.controller;

import com.example.miniproyecto2.model.Sudoku;
import com.example.miniproyecto2.view.GameStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * JavaFX controller for the main Sudoku game view.
 *
 * <p>This class manages the interaction between the UI and the {@link Sudoku} model.
 * Responsibilities include:</p>
 * <ul>
 *   <li>Handling cell selection and keyboard input.</li>
 *   <li>Refreshing the grid with current values from the model.</li>
 *   <li>Applying visual highlights (selection, invalid candidates, hints).</li>
 *   <li>Managing the start screen (intro image + play button).</li>
 *   <li>Providing hints via the "Pista" button.</li>
 * </ul>
 *
 * @author Nerie Vasquez Diaz (2519638) & Jose David Hurtado (2519500)
 * @version 1.0
 */
public class GameController {

    /** Main Sudoku grid panel containing all cell buttons. */
    @FXML public GridPane gridPanel;
    public Button restart;

    /** Current selected cell position: [col, row]. */
    private int[] position = new int[2];

    /** Root pane of the scene, used to request focus. */
    @FXML private AnchorPane rootPane;

    /** Intro image displayed before the game starts. */
    @FXML private ImageView imgInicio;

    /** Button to start the game. */
    @FXML private Button btnJugar;

    /** Button to request a hint. */
    @FXML private Button btnPista;

    /** Indicates if a cell is currently focused for input. */
    private Boolean focus = false;

    /** Flag to track if the game is still in the initial state. */
    private Boolean init = true;

    /** Number currently selected by the user. */
    private int selectedNumber = 0;

    /** CSS style classes used to represent different cell states. */
    private static final List<String> CELL_STATE_CLASSES = List.of(
            "cell-fixed",
            "cell-user",
            "cell-hint",
            "cell-line-highlight",
            "cell-invalid-candidate",
            "cell-selected"
    );

    /**
     * Initializes the controller after FXML loading.
     * <p>Sets base styles for grid cells and hides the grid and hint button until the game starts.</p>
     */
    @FXML
    public void initialize() {
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

    /**
     * Retrieves the button located at a specific column and row in the grid.
     *
     * @param col column index (0–5)
     * @param row row index (0–5)
     * @return the Button at the given position, or null if not found
     */
    public Button getButtonAt(int col, int row) {
        for (Node node : gridPanel.getChildren()) {
            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);

            int colIndex = Objects.requireNonNullElse(c, 0);
            int rowIndex = Objects.requireNonNullElse(r, 0);

            if (colIndex == col && rowIndex == row) {
                return (Button) node;
            }
        }
        return null;
    }

    /**
     * Handles a click on a Sudoku cell button.
     * <p>Updates the current position, retrieves the value from the model,
     * and applies selection highlights.</p>
     *
     * @param actionEvent event triggered by button click
     */
    public void buttonAction(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        position = getButtonData(button);
        focus = true;


        selectedNumber = Sudoku.getInstance().infoGrid(position[0], position[1]);
        applySelectionHighlights();
    }

    /**
     * Extracts cell coordinates from a button's userData.
     *
     * @param button Sudoku cell button
     * @return array [col, row] or [0,0] if parsing fails
     */
    public int[] getButtonData(Button button) {
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

    /**
     * Checks if the given character is a valid Sudoku input (1–6).
     *
     * @param letter character to validate
     * @return true if valid, false otherwise
     */
    Boolean checkInput(char letter) {
        char[] numbers = {'0','1', '2', '3', '4', '5', '6'};
        for (char number : numbers) {
            if (number == letter) return true;
        }
        return false;
    }

    /**
     * Sends user input to the Sudoku model and updates the grid if valid.
     *
     * @param letter character entered by the user
     */
    void sendUserInput(char letter) {
        if (checkInput(letter)) {
            if (Sudoku.getInstance().sendInput(position[0], position[1], letter - '0')) {
                focus = false;
                selectedNumber = letter - '0';
                refreshGrid();
                applySelectionHighlights();
            }
        }
    }

    /**
     * Handles keyboard input when a cell is focused.
     *
     * @param event key press event
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        String input = event.getText();
        try {
            if (focus) {
                sendUserInput(input.charAt(0));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Provides a hint by filling one valid cell suggested by the model.
     */
    void hint() {
        int[] hint = Sudoku.getInstance().searchInput();
        if (hint != null) {
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

    /**
     * Refreshes the entire grid with current values from the Sudoku model.
     * <p>Applies styles depending on whether the number is fixed or user-entered.</p>
     */
    private void refreshGrid() {
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

    /**
     * Removes all state-related CSS classes from a button.
     *
     * @param b button to clear styles from
     */
    private void clearCellStateClasses(Button b) {
        b.getStyleClass().removeAll(CELL_STATE_CLASSES);
    }

    /**
     * Applies visual highlights for the currently selected cell and number.
     * <p>Highlights the entire row and column of the selected cell, and marks
     * invalid candidate positions where the selected number cannot be placed.</p>
     */
    private void applySelectionHighlights() {
        if (gridPanel == null) return;

        // Clear dynamic highlights
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

        // Highlight selected cell
        Button selectedButton = getButtonAt(selectedCol, selectedRow);
        if (selectedButton != null) {
            selectedButton.getStyleClass().add("cell-selected");
        }

        // Highlight row and column
        for (int c = 0; c <= 5; c++) {
            Button b = getButtonAt(c, selectedRow);
            if (b != null) b.getStyleClass().add("cell-line-highlight");
        }
        for (int r = 0; r <= 5; r++) {
            Button b = getButtonAt(selectedCol, r);
            if (b != null) b.getStyleClass().add("cell-line-highlight");
        }

        // Mark invalid candidate positions in row/column
        for (int c = 0; c <= 5; c++) {
            markInvalidCandidateIfNeeded(c, selectedRow, selectedNumber);
        }
        for (int r = 0; r <= 5; r++) {
            markInvalidCandidateIfNeeded(selectedCol, r, selectedNumber);
        }
    }

    /**
     * Marks a cell as an invalid candidate for the selected number.
     * <p>Only applies to empty, non-original cells where the number cannot be placed.</p>
     *
     * @param col column index
     * @param row row index
     * @param number number being tested
     */
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

    /**
     * Handles the "Play" button click.
     * <p>Hides the intro screen, shows the grid and hint button, initializes the game logic,
     * and refreshes the grid.</p>
     */
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

        // Start game logic
        if (rootPane != null) {
            rootPane.requestFocus();
        }
        refreshGrid();
        init = false;
        applySelectionHighlights();
        restart.setDisable(false);
        restart.setVisible(true);
    }

    /**
     * Handles the "Hint" button click.
     * <p>Requests a hint from the model, updates the grid, and reapplies highlights.</p>
     */
    @FXML
    private void onPistaClick() {
        hint();
        applySelectionHighlights();
    }
    /**
     * Restart the game
     */
    @FXML
    public void recharge() throws IOException {
        GameStage.getInstance().changeScene("/com/example/miniproyecto2/ViewGame.fxml");
        Sudoku.restartInstance();

    }
}
