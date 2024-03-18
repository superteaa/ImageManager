package sample.component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.bean.ImageBean;
import sample.controller.ImageDisplayController;
import sample.map.ControllerMap;

import java.io.IOException;
import java.util.List;

/**
 * @author 12242
 * @date 2021/05/19
 */
public class DisplayWindow extends VBox {
    private Stage imageStage;
    private static DisplayWindow displayWindow;
    private ImageDisplayController imageDisplayController;

    static {
        try {
            displayWindow = new DisplayWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DisplayWindow() throws IOException {
        VBox root = FXMLLoader.load(getClass().getClassLoader().getResource("展示窗口.fxml"));
        imageDisplayController = (ImageDisplayController) ControllerMap.get("ImageDisplayController");
        imageStage = new Stage();
        imageStage.setScene(new Scene(root));
        imageStage.setTitle("展示窗口");
        imageStage.setResizable(false);
        imageStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                imageDisplayController.getAnimation().stop();
            }
        });
    }

    public Stage getImageStage() {
        return imageStage;
    }

    public static void showDisplayWindow(String curPath, List<ImageBean> imageBeanList) {
        ImageDisplayController imageDisplayController = (ImageDisplayController) ControllerMap.get("ImageDisplayController");
        imageDisplayController.init(curPath, imageBeanList);
        System.out.println("打开展示窗口");
        displayWindow.getImageStage().show(); // 显示页面
    }

}
