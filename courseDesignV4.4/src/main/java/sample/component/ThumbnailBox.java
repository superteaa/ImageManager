package sample.component;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sample.bean.ImageBean;
import sample.bean.SelectedObj;
import sample.controller.Controller;
import sample.controller.ImageDisplayController;
import sample.map.ControllerMap;
import sample.utils.FileUtils;
import sample.utils.ImageUtils;

import java.io.File;
import java.util.List;

/**
 * 缩略图bean
 * @author 12242
 * @date 2021/05/09
 */
public class ThumbnailBox extends VBox {
    private ImageBox imageBox;
    private ImageBean imageBean;
    private Label imageLabel;//图片名
    public boolean isSelected = false;//是否选中
    private Tooltip tooltip;
    private RightBtnMenu rightBtnMenu;

    public ImageBox getImageBox() {
        return imageBox;
    }

    public ThumbnailBox(ImageBean imageBean) {
        this.imageBean = imageBean;
        ImageView imageView = new ImageView(new Image("file:/" + imageBean.getPath(),
                200,
                200,
                true,
                true,
                true));
        Image image = new Image("file:/" + imageBean.getPath(),
                200,
                200,
                true,
                true,
                true);
        System.out.println(image.getHeight());

        this.imageLabel = new Label(imageBean.getImageName() + "." + imageBean.getImageSuffix());
        this.imageBox = new ImageBox(imageView);
        getChildren().addAll(imageBox, imageLabel);
        rightBtnMenu = RightBtnMenu.getRightBtnMenu();
        setAlignment(Pos.CENTER);
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Controller controller = (Controller) ControllerMap.get("Controller");
                SelectedObj selectedObj = controller.getSelectedObj();
                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    if (event.isControlDown()) {
                        if (!ThumbnailBox.this.isSelected) {
                            selectedObj.select(ThumbnailBox.this);
                        } else {
                            selectedObj.unselect(ThumbnailBox.this);
                        }
                    } else{
                        selectedObj.clear();
                        selectedObj.select(ThumbnailBox.this);
                    }
                    if (rightBtnMenu.isShowing()) {
                        rightBtnMenu.hide();
                    }
                } else if (event.getClickCount() == 2) {
                    showImageDisplay(imageBean.getPath());
                }
                if (event.isSecondaryButtonDown()) {
                    rightBtnMenu.show(ThumbnailBox.this,event.getScreenX(),event.getScreenY());
                }
                controller.showImageStatus();
            }
        });

        tooltip = new Tooltip(imageBean.getImageName() + "." + imageBean.getImageSuffix() + "\n" +
                "最后修改时间: " + ImageUtils.getFormatDate(imageBean.getLastModified()) + "\n" +
                "大小: " + String.format("%.2f",ImageUtils.getSingleSizeByMetabyte(imageBean)) + " MB");
        Tooltip.install(this, tooltip);
    }


    public Label getImageLabel() {
        return imageLabel;
    }

    public ImageBean getImageBean() {
        return imageBean;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setImageBean(ImageBean imageBean) {
        this.imageBean = imageBean;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setImageLabel(Label imageLabel) {
        this.imageLabel = imageLabel;
    }

    public void showImageDisplay(String path){
        try {
            Controller controller = (Controller) ControllerMap.get("Controller");
            List<ImageBean> imageBeanList = FileUtils.getImageBeanList(new File(controller.getPath()));
            controller.sortImageBeanList(imageBeanList, controller.getSortBox().getSelectionModel().getSelectedIndex());
            DisplayWindow.showDisplayWindow(path, imageBeanList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showImagePath(){
        Controller controller = (Controller) ControllerMap.get("Controller");
        controller.imageLocation.setText(this.imageBean.getPath());
    }

    public void unshowImagePath(){
        Controller controller = (Controller) ControllerMap.get("Controller");
        controller.imageLocation.setText("");
    }

}

