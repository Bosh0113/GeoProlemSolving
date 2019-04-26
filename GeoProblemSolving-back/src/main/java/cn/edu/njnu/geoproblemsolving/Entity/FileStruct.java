package cn.edu.njnu.geoproblemsolving.Entity;

import java.util.ArrayList;

/**
 * Created by SongJie on 2019/4/26 10:15
 */
public class FileStruct {
    private String name;
    private String uid;
    private ArrayList<FileStruct> folders;
    private ArrayList<FileNode> files;

    public FileStruct() {
    }

    public FileStruct(String name, String uid, ArrayList<FileStruct> folders, ArrayList<FileNode> files) {
        this.name = name;
        this.uid = uid;
        this.folders = folders;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<FileStruct> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<FileStruct> folders) {
        this.folders = folders;
    }

    public ArrayList<FileNode> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileNode> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "FileStruct{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", folders=" + folders +
                ", files=" + files +
                '}';
    }
}

