package sample.utils;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import sample.bean.ImageBean;
import sample.component.AlertDialog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * @author 12242
 * @date 2021/05/09
 */
public class FileUtils {

    /**
     * 返回图片文件列表
     * @param file 文件夹
     * @return 文件夹下的图片文件列表
     */
    public static File[] getImageFileList(File file) {
        if (file == null) {
            System.out.println("file为空");
        }
        return file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".bmg")) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 把图片文件列表包装成ImageBean列表
     * @param files 文件列表
     * @return ImageBean列表
     */
    public static List<ImageBean> getImageBeanList(File[] files) {
        if ( files == null || files.length == 0 ) {
            return null;
        }
        List<ImageBean> list = new ArrayList<>();
        for (File file : files) {
            list.add(new ImageBean(file));
        }
        return list;
    }

    /**
     * 返回Imagebean列表
     * @param file 文件夹
     * @return 文件夹下的ImageBean列表
     */
    public static List<ImageBean> getImageBeanList(File file) {
        File[] imageFileList = getImageFileList(file);
        return getImageBeanList(imageFileList);
    }


    /**
     * 更改文件名
     * @param beanFile 要更改的file对象
     * @param newName 新名字（不是绝对路径）
     * @return
     */
    public static boolean renameFile(File beanFile, String newName) {
        File newFile = new File(beanFile.getParent() + File.separator + newName);
        return beanFile.renameTo(newFile);
    }

    /**
     * @param src 源文件url的file
     * @param dst 新文件url的file
     * @return 0为复制成功 1为dst已存在 2为src不存在 3为复制失败
     */
    public static int copyFileToDir(File src, File dst) {
        if (dst.exists()) {
            return 1;
        }
        if (!src.exists()) {
            return 2;
        }
        try (FileInputStream is = new FileInputStream(src);
             FileOutputStream os = new FileOutputStream(dst)){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 3;
        }
        return 0;
    }

}
