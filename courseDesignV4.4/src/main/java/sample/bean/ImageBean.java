package sample.bean;

import sample.utils.ImageUtils;
import java.awt.*;
import java.io.File;


public class ImageBean {
    private File file;
    private String path;//绝对路径
    private String imageName;//图片名
    private String imageSuffix;//后缀名
    private Long size;//大小（单位:byte）
    private Long lastModified;//最后修改时间（时间戳形式）

    public StringBuilder http;



    public ImageBean(File file) {
        this.file = file;
        this.path = file.getAbsolutePath();
        this.imageName = ImageUtils.getName(file.getName());
        this.imageSuffix = ImageUtils.getSuffix(file.getName());
        this.size = file.length();
        this.lastModified = file.lastModified();
    }


    public File getFile() {
        return file;
    }

    /**
     * @return 绝对路径
     */
    public String getPath() {
        return path;
    }

    /**
     * @return 不含后缀名的文件名
     */
    public String getImageName() {
        return imageName;
    }

    public String getImageSuffix() {
        return imageSuffix;
    }

    public Long getSize() {
        return size;
    }

    public Long getLastModified() {
        return lastModified;
    }
}
