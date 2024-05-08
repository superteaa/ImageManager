package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.controller.Controller;
import sample.controller.ImageDisplayController;
import sample.map.ControllerMap;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getClassLoader().getResource("主窗口布局图.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

    Parent root = fxmlLoader.load();
        ControllerMap.put(fxmlLoader.getController());

        Scene scene = new Scene(root);//
        scene.setFill(Color.LIGHTBLUE);//刚改的，想改背景颜色，失败

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("美图看看");
        System.out.println("打开预览窗口");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
