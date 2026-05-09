package com.example.miniproyecto2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Singleton class that represents the main application window (Stage) in JavaFX.
 *
 * This class manages the primary stage of the application and provides functionality
 * to dynamically change scenes based on FXML files. It follows the Singleton pattern
 * to ensure only one instance of the stage exists throughout the application's lifecycle.
 */
public class GameStage extends Stage {

    /**
     * Private constructor for the GameStage singleton.
     *
     * Initializes the stage by loading the main game FXML scene.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    private GameStage() throws IOException{
        setTitle("Sudoku");
        changeScene("/com/example/miniproyecto2/ViewGame.fxml");
    }

    /**
     * Changes the current scene displayed in the stage to load the specified FXML file.
     *
     * This method loads an FXML file from the classpath, creates a new scene, and displays it
     * in the stage. The stage window is non-resizable.
     *
     * @param fxml the resource path to the FXML file to load
     * @throws IOException if the FXML file cannot be found or loaded
     */
    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(fxml)
        );

        Parent root = fxmlLoader.load();
        Scene currentScene = new Scene(root);
        setScene(currentScene);
        setResizable(false);
        show();
    }

    /**
     * Holder for the lazy-initialized singleton instance (initialization-on-demand holder idiom).
     */
    private static class GameHolder {
        private static GameStage INSTANCE;
    }

    /**
     * Retrieves the single instance of GameStage (Singleton pattern).
     *
     * If the instance does not exist, it creates one by initializing a new GameStage.
     *
     * @return the single instance of GameStage
     * @throws IOException if the GameStage cannot be instantiated due to FXML loading errors
     */
    public static GameStage getInstance() throws IOException{
        if(GameHolder.INSTANCE == null){
            GameHolder.INSTANCE = new GameStage();
        }

        return GameHolder.INSTANCE;
    }
}