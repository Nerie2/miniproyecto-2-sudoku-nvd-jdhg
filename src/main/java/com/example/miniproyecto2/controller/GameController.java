package com.example.miniproyecto2.controller;

import com.example.miniproyecto2.model.Sudoku;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.util.Objects;


public class GameController {

    public GridPane gridPanel;
    int[] position = new int[2];
    @FXML
    private AnchorPane rootPane;
    Boolean focus = false;
    Boolean init = true;
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
                refreshGrid();
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
            getButtonAt(hint[0], hint[1]).setText(text);
            getButtonAt(hint[0], hint[1]).setStyle(
                    "-fx-background-color: #438a30;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 10;"
            );
        }

    }


 private void refreshGrid(){
     for (int i = 0; i <=5; i++) {
         for (int j = 0; j <=5; j++) {

             getButtonAt(i ,j).setText(String.valueOf(Sudoku.getInstance().infoGrid(i , j)));
             if(Sudoku.getInstance().infoGrid(i , j)!=0){
                Button button = getButtonAt(i ,j);
                button.setText(String.valueOf(Sudoku.getInstance().infoGrid(i , j)));
                if(init){
                    button.setStyle(
                            "-fx-background-color: #3c3636;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-radius: 10;"
                    );
                }
             }
         }
     }

 }

    @FXML
    private void onBotonClick() {

        // Aquí puedes iniciar la lógica del juego
        if(rootPane!=null){
            rootPane.requestFocus();
        }
        refreshGrid();
        init = false;
        hint();


    }



}
