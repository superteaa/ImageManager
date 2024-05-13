package sample.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import sample.bean.ImageBean;
import sample.component.DisplayWindow;
import sample.map.ControllerMap;
import sample.utils.FileUtils;

public class NewWindowController implements Initializable{

    public ArrayList<String> imageBeanList;

    private ImageView imageView;

    public int index;

    private Image image;

    private ArrayList<String> urls;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            initText();
    }

    public NewWindowController() throws IOException{
        ControllerMap.put(this);
    }

    public void initText()
    {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void Login(){
        try {
            String curPath = GetPost().get(0);
            DisplayWindow.showDisplayWindowWeb(curPath,GetPost());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    发送post请求
     */
    public  ArrayList<String> GetPost() throws IOException {
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
        String username = usernameField.getText();
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
            // 初始化 Gson
            Gson gson = new Gson();

            // 解析 JSON 字符串为字符串数组
            ArrayList<String> urls = gson.fromJson(response.toString(), ArrayList.class);

            return urls;
//            System.out.println("Server Response: " + response.toString());
        } else {
            System.out.println("HTTP request failed with response code: " + responseCode);
            return null;
        }
    }

}
