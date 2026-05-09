package com.example.miniproyecto2.model;

/**
 * Contract for Sudoku game operations used by the UI/controller.
 *
 * <p>Provides validation, input placement, hint retrieval, board inspection,
 * and access to whether a cell is part of the original puzzle.</p>
 */
public interface ISudoku {
    boolean checkInput(int x, int y, int number);

    Boolean sendInput(int x, int y, int number);
    int[] searchInput();
    int infoGrid(int x , int y);


    void fillSudoku();
    Boolean checkOriginalNum(int x, int y);

}
