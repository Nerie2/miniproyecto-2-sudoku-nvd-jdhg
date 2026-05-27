package com.example.miniproyecto2.model;

/**
 * Singleton facade for the Sudoku game model.
 *
 * <p>Extends {@link AbstractSudoku}, which builds the board and rules on construction.
 * Use {@link #getInstance()} to access the single shared game state from the UI.</p>
 */
public class Sudoku extends AbstractSudoku {


    private static  Sudoku INSTANCE = new Sudoku();


    private Sudoku() {}

    /**
     * This function helps to obtain the correct instance (the only instance).
     * @return the single instance
     */
    public static Sudoku getInstance() {
        return INSTANCE;
    } //singleton
    /**
     * Function of restart
     */
    public static void restartInstance(){
         INSTANCE = new Sudoku();
    }


    /**
     * Optional initializer hook for future use (e.g. resetting the singleton or reloading a puzzle).
     * Currently resolves the instance without changing board state.
     */
    public static void init() {
        Sudoku game = getInstance();

    }



}