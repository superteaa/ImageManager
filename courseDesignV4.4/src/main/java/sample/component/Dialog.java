package sample.component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.controller.NewWindowController;
import sample.map.ControllerMap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.Gson;


public class Dialog extends VBox {
    private Stage DialogStage;
    private static Dialog dialog;
    private static NewWindowController newWindowController;

    static {
        try {
            dialog = new Dialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Dialog() throws IOException {
        VBox root = FXMLLoader.load(getClass().getClassLoader().getResource("窗口.fxml"));
        newWindowController = (NewWindowController) ControllerMap.get("NewWindowController");
        DialogStage = new Stage();
        DialogStage.setScene(new Scene(root));
        DialogStage.setTitle("登录窗口");
        DialogStage.setResizable(false);
    }


    public Stage getDialogStage() {
        return DialogStage;
    }

   /*
    * 展示登录窗口
    */
    public static void showDialogWindow() throws IOException {
        NewWindowController newWindowController = (NewWindowController) ControllerMap.get("NewWindowController");
        System.out.println("打开登录窗口");
        dialog.getDialogStage().show();
    }


}
