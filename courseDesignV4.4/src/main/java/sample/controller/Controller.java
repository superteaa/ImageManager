package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import sample.bean.FileTreeItem;
import sample.bean.ImageBean;
import sample.bean.Point;
import sample.component.AlertDialog;
import sample.component.DisplayWindow;
import sample.component.Dialog;
import sample.component.RightBtnMenu;
import sample.bean.SelectedObj;
import sample.component.ThumbnailBox;
import sample.map.ControllerMap;
import sample.utils.FileUtils;
import sample.utils.ImageUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static File ROOT_FILE = FileSystemView.getFileSystemView().getRoots()[0];
    @FXML
    private TreeView<String> tv_fileTree;

    @FXML
    private TilePane thumbnails;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label imageStatus;

    @FXML
    private ChoiceBox sortBox;

    private Pane pane;

    private Rectangle rectangle;

    private FileTreeItem selectedItem; //目录树现行选中项

    private SelectedObj selectedObj = new SelectedObj(this);

    private List<ImageBean> imageBeanList;

    private Point tilePaneMouseStart;
    private Point tilePaneMouseDragged;

    @FXML
    private ImageView fileImage;

    //当前目录
    private String path;
    @FXML
    public TextField imageLocation;

    // 当用户点击按钮时调用此方法，选择图片并更新标签文本
    @FXML

    private void initPath(){
        imageLocation.setText(" ");
    }

    public String getPath() {
        return path;
    }


    public TilePane getThumbnails() {
        return thumbnails;
    }

    public SelectedObj getSelectedObj() {
        return selectedObj;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPath();
        initThumbnailPane();
        initSortBox();
        initRectangle();
        initFileTree();
        System.out.println("Controller加载完成");
    }

    /**
     * 初始化目录树
     */
    private void initFileTree() {
        FileTreeItem fileTreeItem = new FileTreeItem(ROOT_FILE, f -> {
            File[] allFiles = f.listFiles();
            File[] directorFiles = f.listFiles(File::isDirectory);
            List<File> list = new ArrayList<>(Arrays.asList(allFiles));
            list.removeAll(Arrays.asList(directorFiles));
            return list.toArray(new File[list.size()]);
        });

        tv_fileTree.setRoot(fileTreeItem);
        //目录树被点击
        tv_fileTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1)
                {
                    selectedItem = (FileTreeItem) tv_fileTree.getSelectionModel().getSelectedItem(); //树结点现行选中项更新
                    if(selectedItem == null) {
                        return;
                    }
                    File folder = selectedItem.getFile();
                    if (folder != null) {
                        path = folder.getAbsolutePath();
                        System.out.println("切换目录为" + path);
                        imageBeanList = FileUtils.getImageBeanList(folder);
                        if (imageBeanList != null) {
                            System.out.println("该目录有图片");
                            fileImage.setImage(new Image("file2.png"));
                        } else {
                            System.out.println("该目录无图片");
                            fileImage.setImage(new Image("file.png"));
                        }
                        sortBox.setValue("按名称排序");
                        //刷新被选中图片
                        selectedObj.refresh();
                        //刷新缩略图
                        showThumbnails();
                        //刷新左下角文本
                        showImageStatus();
                    }
                }
            }
        });
    }

    /**
     * 初始化框选矩形，框选时显现
     */
    private void initRectangle() {
        pane = new Pane();
        rectangle = new Rectangle();
        pane.getChildren().addAll(thumbnails, rectangle);
        scrollPane.setContent(pane);
        rectangle.setVisible(false);
    }


    /**
     * 初始化排序按钮
     */
    private void initSortBox() {
        sortBox.setItems(FXCollections.observableArrayList(
                "按大小排序",
                "按修改时间排序",
                "按名称排序"
        ));
        sortBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (imageBeanList == null) {
                    return;
                }
                imageBeanList = FileUtils.getImageBeanList(new File(path));
//                System.out.println(newValue.intValue());
                sortImageBeanList(imageBeanList, newValue.intValue());
                showThumbnails();
            }
        });
    }

    /**
     * 配合sortBox使用，索引对应其选项
     * @param list
     * @param index
     */
    public void sortImageBeanList(List<ImageBean> list, int index) {
        switch(index) {
            //按大小排序
            case 0:
                ImageUtils.sortBySize(list);
                break;
            //按修改时间排序
            case 1:
                ImageUtils.sortByLastModifiedTime(list);
                break;
            //按名称排序
            case 2:
                ImageUtils.sortByName(list);
                break;
            default:
        }
    }

    /**
     * 刷新缩略图列表
     */
    public void showThumbnails() {
        thumbnails.getChildren().clear();//清空上次加载的图片
        if (imageBeanList == null) {
            return;
        }
//        System.out.println(imageBeanList.size());
        for (int i = 0; i < imageBeanList.size(); i++) {
            ThumbnailBox box = new ThumbnailBox(imageBeanList.get(i));
            thumbnails.getChildren().add(box);
        }
//        System.out.println(thumbnails.getChildren().size());
    }


    /**
     * 刷新左下标签
     */
    public void showImageStatus() {
        //点击时初始图片数量（不包括之后刷新）
        ObservableList<Node> children = thumbnails.getChildren();
        int num = children.size();
        double size = 0.00;
        int selected = 0;
        for (int i = 0; i < num; i++) {
            ThumbnailBox node = (ThumbnailBox) children.get(i);
            size += ImageUtils.getSingleSizeByMetabyte(node.getImageBean());
            selected += node.isSelected() ? 1:0;
        }
        imageStatus.setText(num + "张图片（" + String.format("%2.2f", size) +"MB) - 选中" + selected + "张图片");
    }

    public ChoiceBox getSortBox() {
        return sortBox;
    }

    /**
     * 打开展示窗口，并从第一张开始
     */
    public void imageDisplay() {
        try {
            //刷新图片列表
            List<ImageBean> imageBeanList = FileUtils.getImageBeanList(new File(path));
            sortImageBeanList(imageBeanList, sortBox.getSelectionModel().getSelectedIndex());
            DisplayWindow.showDisplayWindow(imageBeanList.get(0).getPath(), imageBeanList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 点击删除图案
     */
    public void ImageDelete() {
        selectedObj.delete();
    }

    /*
    *  点击进入网盘
    */
    public void ImageWeb(){
        try{
            Dialog.showDialogWindow();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 当框选时矩形出现
     * @param start
     * @param end
     */
    private void drawRectangle(Point start, Point end){
        rectangle.setVisible(true);
        rectangle.toFront();
        rectangle.setLayoutX(start.x);
        rectangle.setLayoutY(start.y);
        rectangle.setWidth(end.x - start.x);
        rectangle.setHeight(end.y - start.y);
        rectangle.setStyle("-fx-border-color: #6e7a7f;");
        rectangle.setFill(Paint.valueOf("6e7a7f33"));
    }

    /**
     * 初始化放缩略图的tilepane布局和scrollpane
     */
    private void initThumbnailPane() {
        scrollPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                        if (RightBtnMenu.getRightBtnMenu().isShowing()) {
                            RightBtnMenu.getRightBtnMenu().hide();
                        }

                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        RightBtnMenu.getRightBtnMenu().show(scrollPane, event.getScreenX(), event.getScreenY());
                    }
            }
        });


        thumbnails.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    tilePaneMouseStart = new Point(event.getX(), event.getY());
//                    System.out.println(tilePaneMouseStart.x+ " " + tilePaneMouseStart.y);
                }
            }
        });

        thumbnails.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    selectedObj.clear();
                    tilePaneMouseDragged = new Point(event.getX(), event.getY());
//                    System.out.println(tilePaneMouseDragged.x+ " " + tilePaneMouseDragged.y);
                    if(tilePaneMouseStart.x == tilePaneMouseDragged.x && tilePaneMouseStart.y == tilePaneMouseDragged.y){
                        return;
                    }
                    Point t = new Point(Math.max(tilePaneMouseStart.x, tilePaneMouseDragged.x), Math.max(tilePaneMouseStart.y, tilePaneMouseDragged.y));
                    Point tilePaneMouseStart_t = new Point(Math.min(tilePaneMouseStart.x, tilePaneMouseDragged.x), Math.min(tilePaneMouseStart.y, tilePaneMouseDragged.y));
                    tilePaneMouseDragged = t;
                    //System.out.println("tilePaneMouseStart:" + tilePaneMouseStart_t.x + "," + tilePaneMouseStart_t.y);
                    //System.out.println("tilePaneMouseDragged:" + tilePaneMouseDragged.x + "," + tilePaneMouseDragged.y);
                    for(int i=0; i<thumbnails.getChildren().size() ;i++){
                        double tileLeft = i % ((int)(thumbnails.getWidth() / thumbnails.getTileWidth())) * thumbnails.getTileWidth();
                        double tileTop = i / ((int)(thumbnails.getWidth() / thumbnails.getTileWidth())) * thumbnails.getTileHeight();
                        double tileRight = tileLeft + thumbnails.getTileWidth();
                        double tileButtom = tileTop + thumbnails.getTileHeight();
                        if(!(tileRight < tilePaneMouseStart_t.x || tileLeft > tilePaneMouseDragged.x ||
                                tileTop > tilePaneMouseDragged.y || tileButtom < tilePaneMouseStart_t.y)){ //两矩形重叠
                            selectedObj.select((ThumbnailBox) thumbnails.getChildren().get(i));
                        }
                        else{
                            if(((ThumbnailBox) thumbnails.getChildren().get(i)).isSelected()){ //如果被选中但又不和选框重叠
                                selectedObj.unselect((ThumbnailBox) thumbnails.getChildren().get(i)); //取消选中
                            }
                        }
                    }
                    drawRectangle(tilePaneMouseStart_t, tilePaneMouseDragged);
                }
                showImageStatus();
            }
        });
        thumbnails.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rectangle.setVisible(false);
            }
        });

    }

    public void showInfo() {
        AlertDialog.getDialog(AlertDialog.DialogType.CREW);

    }



}
