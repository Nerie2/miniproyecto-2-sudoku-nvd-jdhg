package com.example.miniproyecto2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base implementation for a 6x6 Sudoku-like game.
 *
 * <p>This abstract class defines the core rules and board manipulation logic:
 * validating inputs, tracking original (fixed) cells, generating a filled board,
 * and removing cells to create hints/challenges.</p>
 *
 * <p>It uses a {@link NodeMatrix} structure to store values and flags for each cell.</p>
 *
 * @author Nerie & Jose David Hurtado
 * @version 1.0
 */
public abstract class AbstractSudoku implements ISudoku {

    /** Recursive node-based matrix representing the Sudoku board. */
    NodeMatrix tablero = new NodeMatrix();

    /** List of hints (cells removed from the board) stored as [x, y, value]. */
    List<int[]> listHints = new ArrayList<>();

    /** Constructor: initializes the board by filling it and generating hints. */
    public AbstractSudoku() {
        fillSudoku();
    }

    /**
     * Validates whether a number can be placed at the given coordinates.
     *
     * <p>Checks:</p>
     * <ul>
     *   <li>Number must be between 1 and 6.</li>
     *   <li>Number must not already exist in the same 3x2 block.</li>
     *   <li>Number must not already exist in the same row.</li>
     *   <li>Number must not already exist in the same column.</li>
     * </ul>
     *
     * @param x column index (0–5)
     * @param y row index (0–5)
     * @param number number to validate
     * @return true if valid, false otherwise
     */
    @Override
    public boolean checkInput(int x, int y, int number) {
        if (number < 1 || number > 6) return false;

        int starRow = (x > 2) ? 3 : 0;
        int starCol = (y / 2) * 2;

        // Check block
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (tablero.getValue(starRow + i, starCol + j) == number) {
                    return false;
                }
            }
        }

        // Check row
        for (int col = 0; col < 6; col++) {
            if (tablero.getValue(x, col) == number) return false;
        }

        // Check column
        for (int row = 0; row < 6; row++) {
            if (tablero.getValue(row, y) == number) return false;
        }

        return true;
    }

    /**
     * Checks if the cell at the given coordinates is part of the original puzzle.
     *
     * @param x column index
     * @param y row index
     * @return true if original, false otherwise
     */
    @Override
    public Boolean checkOriginalNum(int x, int y) {
        return tablero.getOriginal(x, y);
    }

    /**
     * Attempts to place a number in the given cell.
     *
     * <p>Conditions:</p>
     * <ul>
     *   <li>Number must be valid according to {@link #checkInput}.</li>
     *   <li>Cell must not be part of the original puzzle.</li>
     *   <li>If the cell was a hint, it is removed from the hint list.</li>
     * </ul>
     *
     * @param x column index
     * @param y row index
     * @param number number to place
     * @return true if successfully placed, false otherwise
     */
    @Override
    public Boolean sendInput(int x, int y, int number) {
        if (checkInput(x, y, number) && !checkOriginalNum(x, y)) {
            if (listHints != null && !listHints.isEmpty()) {
                int[] hint = listHints.get(0);
                if (hint[0] == x && hint[1] == y && hint[2] == number) {
                    listHints.remove(0);
                }
            }
            tablero.addInfo(x, y, number);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the next available hint from the list.
     *
     * @return array [x, y, value] or null if no hints available
     */
    @Override
    public int[] searchInput() {
        if (listHints == null) return null;
        if (!listHints.isEmpty()) return listHints.get(0);
        return null;
    }

    /**
     * Retrieves the value stored in the given cell.
     *
     * @param x column index
     * @param y row index
     * @return integer value (0 if empty)
     */
    @Override
    public int infoGrid(int x, int y) {
        return tablero.getValue(x, y);
    }

    /**
     * Fills the Sudoku board recursively and generates hints.
     */
    @Override
    public void fillSudoku() {
        fillRecursive(0, 0);
        try {
            makeHint(1);
            makeHint(1);
            makeHint(1);
            makeHint(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates hints by removing numbers from specific blocks.
     *
     * @param block block index (1–6)
     */
    private void makeHint(int block) {
        if (block > 6) return;

        int x = (int) (Math.random() * 3);
        int y = (int) (Math.random() * 2);

        switch (block) {
            case 2 -> x += 3;
            case 3 -> y += 2;
            case 4 -> { x += 3; y += 2; }
            case 5 -> y += 4;
            case 6 -> { y += 4; x += 3; }
        }

        if (addHint(x, y, tablero.getValue(x, y))) {
            makeHint(block + 1);
        } else {
            makeHint(block);
        }
    }

    /**
     * Adds a hint by removing a number from the board and storing it in the hint list.
     *
     * @param x column index
     * @param y row index
     * @param number number to remove
     * @return true if hint added, false otherwise
     */
    private Boolean addHint(int x, int y, int number) {
        if (tablero.getValue(x, y) != 0) {
            listHints.add(new int[]{x, y, number});
            tablero.addInfo(x, y, 0);
            tablero.setOriginal(x, y, false);
            return true;
        }
        return false;
    }

    /**
     * Recursively fills the Sudoku board with valid numbers.
     *
     * @param fila current row
     * @param col current column
     * @return true if successfully filled, false otherwise
     */
    private boolean fillRecursive(int fila, int col) {
        if (col == 6) {
            col = 0;
            fila++;
        }
        if (fila == 6) return true;

        Integer[] numbers = {1, 2, 3, 4, 5, 6};
        List<Integer> listNums = Arrays.asList(numbers);
        Collections.shuffle(listNums);

        for (int num : listNums) {
            if (sendInput(fila, col, num)) {
                if (fillRecursive(fila, col + 1)) {
                    tablero.setOriginal(fila, col, true);
                    return true;
                }
                tablero.setOriginal(fila, col, false);
                tablero.addInfo(fila, col, 0);
            }
        }
        return false;
    }
}
