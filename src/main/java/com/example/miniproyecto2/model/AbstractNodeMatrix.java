package com.example.miniproyecto2.model;

/**
 * Base implementation for a recursive, node-based matrix.
 *
 * <p>This abstract class represents a dynamic matrix where each cell is a node
 * that can recursively point to its neighbors in the X (left) and Y (right) directions.
 * Each node stores an integer value and a flag indicating whether the cell belongs
 * to the original puzzle (immutable/fixed).</p>
 *
 * <p>It provides methods to add and retrieve values, as well as to mark and query
 * whether a cell is part of the original puzzle setup.</p>
 *
 * <p>Concrete implementations should extend this class to define specific behaviors
 * or additional logic for Sudoku or similar grid-based games.</p>
 *
 * @author Nerie & Jose David Hurtado
 * @version 1.0
 */
public abstract class AbstractNodeMatrix implements INodeMatrix {

    /** Reference to the node in the X direction (left). */
    NodeMatrix left;

    /** Reference to the node in the Y direction (right). */
    NodeMatrix right;

    /** Value stored in this cell (0 if empty). */
    int value;

    /** Flag indicating if this cell is part of the original puzzle (fixed). */
    Boolean original = false;

    /** Default constructor. */
    public AbstractNodeMatrix() { }

    /**
     * Retrieves whether the cell at the given coordinates is part of the original puzzle.
     *
     * @param x column index (recursive depth in X direction)
     * @param y row index (recursive depth in Y direction)
     * @return true if the cell is marked as original, false otherwise
     */
    Boolean getOriginal(int x, int y) {
        if (x > 0) {
            if (left == null) return false;
            return left.getOriginal(x - 1, y);
        }
        if (y > 0) {
            if (right == null) return false;
            return right.getOriginal(x, y - 1);
        }
        if (x == 0 && y == 0) {
            return this.original;
        }
        return false;
    }

    /**
     * Sets the "original" flag for the cell at the given coordinates.
     *
     * @param x column index
     * @param y row index
     * @param valueSave true if the cell should be marked as original
     */
    void setOriginal(int x, int y, Boolean valueSave) {
        if (x > 0) {
            if (left == null) {
                left = new NodeMatrix();
            }
            left.setOriginal(x - 1, y, valueSave);
            return;
        }
        if (y > 0) {
            if (right == null) {
                right = new NodeMatrix();
            }
            right.setOriginal(x, y - 1, valueSave);
            return;
        }
        if (x == 0 && y == 0) {
            this.original = valueSave;
        }
    }

    /**
     * Stores a value in the cell at the given coordinates.
     *
     * @param x column index
     * @param y row index
     * @param valueSave value to store in the cell
     */
    @Override
    public void addInfo(int x, int y, int valueSave) {
        if (x > 0) {
            if (left == null) {
                left = new NodeMatrix();
            }
            left.addInfo(x - 1, y, valueSave);
            return;
        }
        if (y > 0) {
            if (right == null) {
                right = new NodeMatrix();
            }
            right.addInfo(x, y - 1, valueSave);
            return;
        }
        if (x == 0 && y == 0) {
            this.value = valueSave;
        }
    }

    /**
     * Retrieves the value stored in the cell at the given coordinates.
     *
     * @param x column index
     * @param y row index
     * @return integer value stored in the cell, or 0 if empty or not initialized
     */
    @Override
    public int getValue(int x, int y) {
        if (x > 0) {
            if (left == null) return 0;
            return left.getValue(x - 1, y);
        }
        if (y > 0) {
            if (right == null) return 0;
            return right.getValue(x, y - 1);
        }
        if (x == 0 && y == 0) {
            return this.value;
        }
        return 0;
    }
}
