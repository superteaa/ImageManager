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

public class Dialog extends VBox {
    private Stage DialogStage;
    private static Dialog dialog;
    private NewWindowController newWindowController;

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

    /*
    发送post请求
     */
    public StringBuilder GetPost() throws IOException {
        String url = "http://175.178.24.103:3388/images/read";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");

        // 设置为发送POST请求
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // 构建JSON格式的请求体
        /**
         * username、password: 从页面读取用户输入
         */
        String username = newWindowController.usernameField.getText();
//        String password = "chh";
//        String jsonBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        String jsonBody = "{\"username\": \"" + username + "\"}";
        // 将JSON请求体写入输出流
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            byte[] data = jsonBody.getBytes(StandardCharsets.UTF_8);
            wr.write(data);
            wr.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            /**
             * response: 服务器返回信息
             */
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response;
//            System.out.println("Server Response: " + response.toString());
        } else {
            System.out.println("HTTP request failed with response code: " + responseCode);
            return null;
        }
    }



    public Stage getDialogStage() {
        return DialogStage;
    }

   /*
    * 展示登录窗口
    */
    public static void showDialogWindow(){
        NewWindowController newWindowController = (NewWindowController) ControllerMap.get("NewWindowController");
        System.out.println("打开登录窗口");
        dialog.getDialogStage().show();
    }


}
