package sample.utils;

import sample.bean.ImageBean;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

/**
 * @author 12242
 * @date 2021/05/09
 *
 * 图片工具类
 */
public class ImageUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    /**
     * 得到图片文件的文件名
     * @param name 完整文件名
     * @return 不含后缀的文件名
     */
    public static String getName(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    /**
     * 得到图片文件的后缀名
     * @param name 完整文件名
     * @return 后缀名
     */
    public static String getSuffix(String name) {
        return name.substring(name.lastIndexOf(".")+1);
    }

    /**
     * 计算图片bean列表的总大小，以单位mb返回
     * @param imageBean
     * @return mb
     */
    public static double getSingleSizeByMetabyte(ImageBean imageBean) {
        return imageBean.getSize()/(1024.0 * 1024);
    }

    public static String getFormatDate(Long time) {
        return simpleDateFormat.format(time);
    }

    public static void sortByLastModifiedTime(List<ImageBean> list) {
        list.sort(new Comparator<ImageBean>() {
            @Override
            public int compare(ImageBean o1, ImageBean o2) {
                if (o1.getLastModified() > o2.getLastModified()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public static void sortBySize(List<ImageBean> list) {
        list.sort(new Comparator<ImageBean>() {
            @Override
            public int compare(ImageBean o1, ImageBean o2) {
                if (o1.getSize() > o2.getSize()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public static void sortByName(List<ImageBean> list) {
        list.sort(new Comparator<ImageBean>() {
            @Override
            public int compare(ImageBean o1, ImageBean o2) {
                return o1.getImageName().compareTo(o2.getImageName());
            }
        });
    }
}
