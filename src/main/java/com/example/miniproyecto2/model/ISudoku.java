package com.example.miniproyecto2.model;

public interface ISudoku {
    boolean checkInput(int x, int y, int number);

    Boolean sendInput(int x, int y, int number);
    int[] searchInput();
    int infoGrid(int x , int y);


    void fillSudoku();
    Boolean checkOriginalNum(int x, int y);

}
