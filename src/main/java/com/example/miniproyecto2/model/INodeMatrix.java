package com.example.miniproyecto2.model;

/**
 * Minimal interface for a matrix-like structure used to store the Sudoku board.
 *
 * <p>The implementation is backed by a recursive node structure rather than a 2D array.</p>
 */
public interface INodeMatrix {
    void addInfo( int x , int y , int valueSave);
     int getValue(int x, int y);
}
