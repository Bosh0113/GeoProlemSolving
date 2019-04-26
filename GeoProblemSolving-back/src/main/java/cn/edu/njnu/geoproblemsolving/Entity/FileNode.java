package cn.edu.njnu.geoproblemsolving.Entity;

/**
 * Created by SongJie on 2019/4/26 10:21
 */
public class FileNode{
    String name;
    String uid;

    public FileNode() {
    }

    public FileNode(String name, String uid) {
        this.name = name;
        this.uid = uid;
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

    @Override
    public String toString() {
        return "FileNode{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
