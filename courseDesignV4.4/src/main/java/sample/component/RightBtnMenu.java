package sample.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;
import sample.bean.ImageBean;
import sample.controller.Controller;
import sample.map.ControllerMap;
import sample.utils.FileUtils;

import javax.sound.sampled.Clip;
import java.io.File;
import java.util.List;

/**
 * 右键菜单
 * @author 12242
 * @date 2021/05/12
 */
public class RightBtnMenu extends ContextMenu {
    private static RightBtnMenu rightBtnMenu = new RightBtnMenu();

    public RightBtnMenu() {
        MenuItem deleteItem = new MenuItem("删除");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller controller = (Controller) ControllerMap.get("Controller");
                if (controller.getSelectedObj().getThumbnailBoxList().size() > 0) {
                    if((AlertDialog.getDialog(AlertDialog.DialogType.ConfirmDeletion))) {
                        controller.getSelectedObj().delete();
                    }
                    controller.showImageStatus();
                }
            }
        });
        MenuItem renameItem = new MenuItem("重命名");
        renameItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller controller = (Controller) ControllerMap.get("Controller");
                controller.getSelectedObj().rename();
                controller.showImageStatus();
            }
        });
        MenuItem copyItem = new MenuItem("复制");
        copyItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                Controller controller = (Controller) ControllerMap.getCtrlMap().get("Controller");
                //得到当前目录图片的文件对象
                List<File> list = controller.getSelectedObj().copy();
                ClipboardContent content = new ClipboardContent();
                content.put(DataFormat.FILES, list);
                clipboard.setContent(content);
            }
        });
        MenuItem pasteItem = new MenuItem("粘贴");
        pasteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller controller = (Controller) ControllerMap.getCtrlMap().get("Controller");
                String parentPath = controller.getPath() + File.separator;
                //打开系统剪切板
                Clipboard clipboard = Clipboard.getSystemClipboard();
                List<File> files = clipboard.getFiles();
                //循环复制
                for (File file : files) {
                    File newFile = new File(parentPath + file.getName());
                    int returnType = FileUtils.copyFileToDir(file, newFile);
                    System.out.println(returnType);
                    //应付不同情况，0成功，1文件存在，2文件未找到，3复制失败
                    if (returnType == 0) {
                        controller.getThumbnails().getChildren().add(new ThumbnailBox(new ImageBean(newFile)));
                    } else if (returnType == 1){
                        AlertDialog.getDialog(AlertDialog.DialogType.FileExist, parentPath + file.getName());
                    } else if (returnType == 2) {
                        AlertDialog.getDialog(AlertDialog.DialogType.FileNotFound, file.getAbsolutePath());
                    } else {
                        AlertDialog.getDialog(AlertDialog.DialogType.CopyFailure);
                    }
                }
                controller.showImageStatus();
            }
        });
        getItems().addAll(deleteItem, renameItem, copyItem, pasteItem);
    }

    public static RightBtnMenu getRightBtnMenu() {
        return rightBtnMenu;
    }
}
