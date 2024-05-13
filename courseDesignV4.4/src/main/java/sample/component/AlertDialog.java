package sample.component;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.text.NumberFormat;
import java.util.Optional;

/**
 * 生成警告对话框
 * @author 12242
 * @date 2021/05/14
 */
public class AlertDialog {
    public static enum DialogType {
        /**
         * 确认删除
         */
        ConfirmDeletion,
        /**
         * 解析数字错误，输入应为正整数
         */
        NumberFormat,

        /**
         * 空文件名
         */
        EmptyFilename,

        /**
         * 文件名重复
         */
        RepeatedFilename,

        /**
         * 相同文件名的文件已存在
         */
        FileExist,

        /**
         * 源文件不存在
         */
        FileNotFound,

        /**
         * 复制过程中发生异常
         */
        CopyFailure,

        /**
         * 制作人员名单
         */
        CREW
    }

    public static boolean getDialog(DialogType dialogType, String file) {
        Alert alert = null;
        switch(dialogType) {

            case ConfirmDeletion:
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("删除");
                alert.setContentText("确定要删除吗?");
                break;
            case NumberFormat:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("格式错误");
                alert.setContentText("请输入正整数");
                break;
            case EmptyFilename:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("格式错误");
                alert.setContentText("文件名不能为空");
                break;
            case RepeatedFilename:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("重命名失败");
                alert.setContentText("文件名 " + file +" 已存在");
                break;
            case FileExist:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("复制成功");
                alert.setContentText("目标文件 " + file + " 已存在");
                break;
            case FileNotFound:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("复制失败");
                alert.setContentText("未找到" + file);
                break;
            case CopyFailure:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("复制失败");
                alert.setContentText("复制过程中发生异常");
                break;
        }
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean getDialog(DialogType dialogType) {
        return getDialog(dialogType,null);
    }

}
