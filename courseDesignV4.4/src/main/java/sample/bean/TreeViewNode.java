package sample.bean;

import sample.DFSLevel;

import java.io.File;

public class TreeViewNode {
    public DFSLevel dfsLevel = DFSLevel.NONE; //结点搜索状态，默认无搜索
    private String path;
    private File folder;

    public TreeViewNode(String path, File folder) {

        this.path = path;
        this.folder = folder;
    }

    @Override
    public String toString(){
        if(folder == null) { //系统根目录
            dfsLevel = DFSLevel.NONE;
            return "此电脑";
        }
        else if(folder.getPath().charAt(folder.getPath().length() - 2) == ':') //磁盘根目录
            return folder.getPath();
        else //普通目录
            return folder.getName();
    }

    public String getPath() {
        return path;
    }

    public File getFolder() {
        return folder;
    }
}
