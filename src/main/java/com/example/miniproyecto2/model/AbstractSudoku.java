package com.example.miniproyecto2.model;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSudoku implements ISudoku{

    NodeMatrix tablero = new NodeMatrix();

    List<int[]> listHints =new ArrayList<>();
    public AbstractSudoku(){
        fillSudoku();
    }
    @Override
    public boolean checkInput(int x, int y,int number) {
        if (number<1 || number>6){
            return false;
        }

        int starRow = 0;
        if(x>2) starRow = 3;
        int starCol= (y/2)*2;


        for (int i =0; i <3; i++){
            for (int j=0; j<2; j++){
                if (tablero.getValue(starRow+i , starCol+j)==number){
                    return false;
                }
            }

        }

        for (int col=0; col<6; col++){
            if (tablero.getValue(x,col)==number){
                return false;
            }

        }

        for (int row=0; row<6; row++){
            if (tablero.getValue(row , y)==number){
                return false;
            }

        }

        return true;

    }
    @Override
    public Boolean checkOriginalNum(int x, int y){

            return tablero.getOriginal(x ,y );



    }

    @Override
    public Boolean sendInput(int x, int y, int number) {
        if (checkInput(x,y,number) && !checkOriginalNum(x, y)){
            if(listHints != null){
                if(!listHints.isEmpty()){
                    int[] hint = listHints.get(0);
                    if(hint[0] == x &&  hint[1] == y && hint[2] == number ){
                        listHints.remove(0);
                    }
                } // revisa si lo que se modifica fue una pista
            }


            tablero.addInfo(x, y , number);
            return true;
        }
        return false;
    }

    @Override
    public int[] searchInput() {
        if(listHints == null) return null;
        if(listHints.get(0) != null) return listHints.get(0);
        return null;


    }
    @Override
    public int infoGrid(int x , int y){
        return tablero.getValue(x , y );

    }
    @Override
    public void fillSudoku() {


        fillRecursive(0, 0);

    try{
        makeHint(1);
        makeHint(1);
        makeHint(1);
        makeHint(1);

    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    }
    private void makeHint( int block){
        if (block>6){
        return;
        }

         int x = (int) (Math.random() * 3);//Se elige un numero aleatorio de 0 a 3
         int y = (int) (Math.random() * 2);// Se elige un numero aleatorio de 0 a 2
        switch (block){
            case 2:
                x+= 3;
                break;
            case 3:
                y+= 2;
                break;
            case 4:
                x+= 3;
                y+= 2;
                break;
            case 5:
                y+=4;
                break;
            case 6:
                y+=4;
                x+=3;
                break;

        }


        if (addHint(x, y, tablero.getValue(x,y))) {

            makeHint(block+1);
        }
        else{
            makeHint(block);
        }

    }

    private Boolean addHint(int x, int y, int number){
        if (tablero.getValue(x,y)!= 0) {
            listHints.add(new int[]{x, y, number});
            tablero.addInfo(x,y, 0);
            tablero.setOriginal(x ,y , false);
            return true;
        }
        return false;

    }

    private boolean fillRecursive(int fila, int col) {

        if (col == 6) {
            col = 0;
            fila++;
        }
        if (fila == 6) return true;


        Integer[] numbers = {1, 2, 3, 4, 5, 6};
        List<Integer> listNums = Arrays.asList(numbers);
        Collections.shuffle(listNums); //random

        for (int num : listNums) {

            if (sendInput(fila, col, num)) {

                // Si sendInput fue true, intentamos llenar la siguiente casilla
                if (fillRecursive(fila, col + 1)) {
                    tablero.setOriginal(fila, col , true);
                    return true;
                }

                //recursivamente se llama
                tablero.setOriginal(fila, col , false);
                tablero.addInfo(fila, col , 0);

            }
        }

        return false; // no se pudo llenar de ninguna manera
    }

}