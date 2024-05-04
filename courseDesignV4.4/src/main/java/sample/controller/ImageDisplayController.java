package sample.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.bean.ImageBean;
import sample.map.ControllerMap;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * @author 12242
 * @date 2021/04/15
 */
public class ImageDisplayController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane stackPane;

    public Timeline getAnimation() {
        return animation;
    }

    @FXML
    private Button slideButton;

    private List<ImageBean> imageBeanList;

    //传进来的图片在列表中的索引
    private int index;

    //当前加载图片
    private Image image;

    //光标最后位置
    private double[] lastPosition = new double[2];

    private Timeline animation;
    private boolean isPlaying = false;

    public ImageDisplayController() throws IOException {
        ControllerMap.put(this);
    }

    private ImageView start;
    private ImageView end;


    /**
     * 设置imageView的事件
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stackPane.setPrefSize(1157.0,654.0);
        initSlide();
        System.out.println("ImageDisplayController加载完成");
    }

    private void initSlide() {
        animation = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                moveRight();
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);

        //按钮图片
        start = new ImageView(new Image("start.png"));
        start.setFitHeight(30);
        start.setFitWidth(30);
        end = new ImageView(new Image("pause.png"));
        end.setFitHeight(30);
        end.setFitWidth(30);
    }

    private void initImageView() {
        //滚轮缩小放大
        imageView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() < 0) {
                    Scale scale = new Scale(0.9, 0.9, event.getX(), event.getY());
                    imageView.getTransforms().add(scale);
                }
                if (event.getDeltaY() > 0) {
                    Scale scale = new Scale(1.1, 1.1, event.getX(), event.getY());
                    imageView.getTransforms().add(scale);
                }
            }
        });

        //按住光标时，记录光标位置
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastPosition[0] = event.getX();
                lastPosition[1] = event.getY();
            }
        });

        //鼠标移动图片
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Translate tran = new Translate(event.getX() - lastPosition[0], event.getY() - lastPosition[1]);
                imageView.getTransforms().add(tran);
            }
        });

        imageView.setPreserveRatio(true);
        double sysRatio = stackPane.getPrefWidth() / stackPane.getPrefHeight();
        double ratio = image.getWidth() / image.getHeight();

        imageView.fitWidthProperty().unbind();
        imageView.fitHeightProperty().unbind();
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);

        if (image.getWidth() >  stackPane.getPrefWidth()|| image.getHeight() > stackPane.getPrefHeight()) {
            if (ratio > sysRatio) {
                if (stackPane.widthProperty().getValue() == 0) {
                    //不能删，删了有bug（误，其实是第一次打开窗口时preferred参数是获取不到的，只能layout后使用（因为默认computed size），
                    //所以第一次只能手动返回stackpane的Size，下同）
                    imageView.fitWidthProperty().bind(new ObservableValueBase<Number>() {
                        @Override
                        public Number getValue() {
                            //stackpane的width
                            return 1157.0;
                        }
                    });
                } else {
                    imageView.fitWidthProperty().bind(stackPane.widthProperty());
                }
            } else {
                if (stackPane.heightProperty().getValue() == 0) {
                    imageView.fitHeightProperty().bind(new ObservableValueBase<Number>() {
                        @Override
                        public Number getValue() {
                            //stackpane的height
                            return 654.0;
                        }
                    });
                } else {
                    imageView.fitHeightProperty().bind(stackPane.heightProperty());
                }
            }
        } else {
            imageView.fitWidthProperty().setValue(image.getWidth());
        }
//        System.out.println(imageView.getFitWidth());
//        System.out.println(imageView.getFitHeight());
    }

    /**
     * 外部打开该Controller时需要做的初始化
     * @param imagePath 打开窗口后第一张展示的图片
     * @param imageBeanList 图片列表
     */
    public void init(String imagePath, List<ImageBean> imageBeanList) {
        this.imageBeanList = imageBeanList;
        for (int i = 0; i < imageBeanList.size(); i++) {
            if (imageBeanList.get(i).getPath().equals(imagePath)) {
                index = i;
                break;
            }
        }
        setImage(imagePath);
    }

    /*setImage(String imagePath): 这是一个公有方法，接受一个字符串参数imagePath，表示图像文件的路径。在方法内部，它首先创建一个新的Image对象，使用指定的图像路径。这个路径是通过拼接file:/和传入的imagePath得到的。

            imageView.setImage(image): 接下来，通过imageView对象的setImage()方法将刚刚创建的Image对象设置为imageView的图像。*/
    public void setImage(String imagePath) {
        image = new Image("file:/" + imagePath);
//        imageView = new ImageView();
//        stackPane.getChildren().clear();
//        stackPane.getChildren().add(imageView);
        imageView.setImage(image);
        initImageView();
        resetImage();
    }

    /**
     * 每次切换时，重置图片大小和位置
     */
/*    这个 resetImage() 方法用于将 imageView 恢复到其默认状态。它首先将 X 和 Y 缩放比例都设置为 1.0，然后清除所有的变换。这样做会确保图像视图没有任何额外的变换，回到初始状态。*/
    private void resetImage() {
        imageView.setScaleX(1.0);
        imageView.setScaleY(1.0);
        imageView.getTransforms().clear();
    }


/*    这段代码定义了一个名为 slide() 的方法。如果 isPlaying 为真，则停止动画并将按钮图标设置为“start”，否则初始化图片并开始动画，将按钮图标设置为“end”。最后，切换 isPlaying 的值以反映当前播放状态。*/
    public void slide() {
        if (isPlaying) {
            animation.stop();
            slideButton.setGraphic(start);
        } else {
            init(imageBeanList.get(0).getPath(), imageBeanList);
            animation.play();
            slideButton.setGraphic(end);
        }
        isPlaying = !isPlaying;
    }

    public void moveLeft(){
        if (index >= 1) {
            index--;
            setImage(imageBeanList.get(index).getPath());
        }
    }

    public void moveRight(){
        if (index < imageBeanList.size()-1) {
            index++;
            setImage(imageBeanList.get(index).getPath());
        }
    }

    public void zoomIn() {
        imageView.setScaleX(imageView.getScaleX() * 1.25);
        imageView.setScaleY(imageView.getScaleY() * 1.25);
    }

    public void zoomOut() {
        imageView.setScaleX(imageView.getScaleX() * 0.75);
        imageView.setScaleY(imageView.getScaleY() * 0.75);
    }


}
