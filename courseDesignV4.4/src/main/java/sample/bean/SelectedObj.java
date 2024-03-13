package sample.bean;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.bean.ImageBean;
import sample.component.AlertDialog;
import sample.component.ThumbnailBox;
import sample.controller.Controller;
import sample.map.ControllerMap;
import sample.utils.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

/**
 * 存放每个选中的缩略图
 * @author 12242
 * @date 2021/05/12
 */
public class SelectedObj {
    public List<ThumbnailBox> getThumbnailBoxList() {
        return thumbnailBoxList;
    }

    //被选中的缩略图列表
    private List<ThumbnailBox> thumbnailBoxList;
    Controller controller;

    //窗口
    private Stage stage;
    private Integer num;
    private Integer digit;
    private String name;

    public SelectedObj(Controller controller) {
        thumbnailBoxList = new ArrayList<>();
        this.controller = controller;
    }

    public void setThumbnailBoxList(List<ThumbnailBox> thumbnailBoxList) {
        this.thumbnailBoxList = thumbnailBoxList;
    }

    /**
     * 切换目录后，刷新选中列表
     */
    public void refresh() {
        thumbnailBoxList.clear();
    }

    /**
     * 选中
     * @param thumbnailBox
     */
    public void select(ThumbnailBox thumbnailBox) {
        thumbnailBox.getImageBox().setStyle("-fx-background-color:#55555550;-fx-border-color: #297ECE;");
        thumbnailBox.setSelected(true);
        thumbnailBoxList.add(thumbnailBox);
    }

    /**
     * 取消选中
     */
    public void unselect(ThumbnailBox thumbnailBox) {
        thumbnailBox.getImageBox().setStyle("-fx-border-color: #1e2f40;");
        thumbnailBox.setSelected(false);
        //遍历查找耗性能，但没能力优化了
        thumbnailBoxList.remove(thumbnailBox);
    }

    /**
     * 删除
     */
    public void delete() {
        for (ThumbnailBox thumbnailBox : thumbnailBoxList) {
            ImageBean imageBean = thumbnailBox.getImageBean();
            imageBean.getFile().delete();
            controller.getThumbnails().getChildren().remove(thumbnailBox);
        }
    }

    /**
     * 清除选中的box
     */
    public void clear() {
        for (ThumbnailBox thumbnailBox : thumbnailBoxList) {
            thumbnailBox.getImageBox().setStyle("-fx-border-color: #1e2f40;");
            thumbnailBox.setSelected(false);
        }
        thumbnailBoxList.clear();
    }


    /**
     * 对选中的图片重命名
     */
    public void rename() {
        if (thumbnailBoxList.size() == 1) {
            ThumbnailBox thumbnailBox = thumbnailBoxList.get(0);
            ImageBean imageBean = thumbnailBox.getImageBean();
            //打开修改框
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("重命名图片");
            dialog.setContentText("新图片名：");
            Optional<String> res = dialog.showAndWait();
            String newName = "";
            if (res.isPresent()) {
                if (!res.get().equals("")) {
                    newName = res.get() + "." + imageBean.getImageSuffix();
                } else {
                    AlertDialog.getDialog(AlertDialog.DialogType.EmptyFilename);
                    return;
                }
            } else {
                return ;
            }
            System.out.println(controller);
            //修改文件
            if (FileUtils.renameFile(imageBean.getFile(), newName)) {
                //成功则修改显示的标签
                thumbnailBox.setImageBean(new ImageBean(new File(controller.getPath()+ File.separator + newName)));
                System.out.println(controller.getPath() + File.separator + newName);
                thumbnailBox.getImageLabel().setText(newName);
            } else {
                //失败弹出警告框
                AlertDialog.getDialog(AlertDialog.DialogType.RepeatedFilename, newName);
            }
        } else if (thumbnailBoxList.size() > 1){
            GridPane gridPane = new GridPane();
            Label nameLabel = new Label("图片名:");
            TextField nameField = new TextField();
            Label numLabel = new Label("起始编号");
            TextField numField = new TextField();
            Label digitLabel = new Label("编号位数:");
            TextField digitField = new TextField();
            Button comfirm = new Button("确认");
            comfirm.setDefaultButton(true);
            comfirm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                        try {
                            num = Integer.parseInt(numField.getText());
                            digit= Integer.parseInt(digitField.getText());
                        } catch (NumberFormatException e) {
                            AlertDialog.getDialog(AlertDialog.DialogType.NumberFormat);
                        }
                        name = nameField.getText();
                        if (num != null && digit != null && name != null) {
                            stage.close();
                        }
                }
            });
            Button cancel = new Button("取消");
            cancel.setCancelButton(true);
            cancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    num = null;
                    digit = null;
                    name = null;
                    stage.close();
                }
            });

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameField, 1, 0);
            gridPane.add(numLabel, 0, 1);
            gridPane.add(numField, 1, 1);
            gridPane.add(digitLabel, 0, 2);
            gridPane.add(digitField, 1, 2);
            gridPane.add(comfirm,0,3);
            gridPane.add(cancel,1,3);
            Scene rename = new Scene(gridPane);
            stage = new Stage();
            stage.setTitle("重命名");
            stage.setScene(rename);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    num = null;
                    digit = null;
                    name = null;
                }
            });
            stage.showAndWait();
            if (num != null && digit != null && name != null) {
                for (int i = 0; i < thumbnailBoxList.size(); i++) {
                    ThumbnailBox thumbnailBox = thumbnailBoxList.get(i);
                    ImageBean imageBean = thumbnailBox.getImageBean();
                    //修改文件
                    String newName = String.format(name + "%0" + digit + "d" + "." + imageBean.getImageSuffix(), num++);
                    if (FileUtils.renameFile(imageBean.getFile(), newName)) {
                        //成功则修改显示的标签
                        thumbnailBox.setImageBean(new ImageBean(new File(controller.getPath()+ File.separator + newName)));
                        thumbnailBox.getImageLabel().setText(newName);
                    } else {
                        AlertDialog.getDialog(AlertDialog.DialogType.RepeatedFilename, newName);
                    }
                }
            }
        }
    }


    /**
     * 把要选中的文件传出，交给外部的clipboard对象
     * @return 选中的文件
     */
    public List<File> copy() {
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < thumbnailBoxList.size(); i++) {
            File file = thumbnailBoxList.get(i).getImageBean().getFile();
            fileList.add(file);
        }
        return fileList;
    }

}
