package cn.edu.njnu.geoproblemsolving.utils;

import cn.edu.njnu.geoproblemsolving.Commen.FileStructConst;
import cn.edu.njnu.geoproblemsolving.Entity.FileNode;
import cn.edu.njnu.geoproblemsolving.Entity.FileStruct;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * Created by SongJie on 2019/4/26 0:56
 */
public class EditJsonUtil {
    public static String updateFileStruct(String fileStructJO,String projectId,String parentId,String id,String name, int requestType){
        if(null==fileStructJO || "".equals(fileStructJO)||"Fail".equals(fileStructJO)){
            JSONObject fileStructJson = new JSONObject();
            fileStructJson.put("folders",new ArrayList<>());
            fileStructJson.put("files",new ArrayList<>());
            fileStructJson.put("uid",projectId);
            fileStructJO = fileStructJson.toJSONString();
        }
        FileStruct fileStruct = JSONObject.parseObject(fileStructJO, FileStruct.class);
        String res = "";
        switch (requestType){
            case FileStructConst.UPLOAD_FILE:
                FileNode fileNode = new FileNode(name, id);
                res = addFileJson(fileStruct, fileNode, parentId);
                break;
            case FileStructConst.DELETE_FILE:
                res = deleteFile(fileStruct,parentId,id);
                break;
            case FileStructConst.CREATE_FOLDER:
                FileStruct folderStruct = new FileStruct(name, id, new ArrayList<>(), new ArrayList<>());
                res = createFolder(fileStruct,parentId,folderStruct);
                break;
            case FileStructConst.DELETE_FOLDER:
                res = deleteFolder(fileStruct,parentId,id);
                break;
            case FileStructConst.RENAME_FOLDER:
                res = renameFolder(fileStruct,parentId,id,name);
                break;
        }

        if("Success".equals(res)){
            return JSONObject.toJSONString(fileStruct);
        }else{
            return "Fail";
        }
    }


    private static String deleteFile(FileStruct fileStruct,String parentId,String id){
        String uid = fileStruct.getUid();
        ArrayList<FileStruct> folders = fileStruct.getFolders();
        ArrayList<FileNode> files = fileStruct.getFiles();
        if(parentId.equals(uid)){
            if(null == files || files.size()<=0){
                return "Fail";
            }
            for(int i =0;i<files.size();i++){
                if(id.equals(files.get(i).getUid())){
                    files.remove(i);
                    return "Success";
                }
            }
        }
        while (folders!=null&&folders.size()>=0){
            for(int i=0;i<folders.size();i++){
                uid = folders.get(i).getUid();
                if(parentId.equals(uid)){
                    files = folders.get(i).getFiles();
                    if(null == files || files.size()<=0){
                        return "Fail";
                    }
                    for(int j =0;j<files.size();j++){
                        if(id.equals(files.get(j).getUid())){
                            files.remove(j);
                            return "Success";
                        }
                    }
                }
                folders = folders.get(i).getFolders();
            }
            if(parentId.equals(uid)){
                break;
            }
        }
        return "Fail";
    }

    private static String renameFolder(FileStruct fileStruct,String parentId,String id,String name){
        String uid = fileStruct.getUid();
        ArrayList<FileStruct> folders = fileStruct.getFolders();
        if(parentId.equals(uid)){
            if(null == folders || folders.size()<=0){
                return "Fail";
            }
            for(int i =0;i<folders.size();i++){
                if(id.equals(folders.get(i).getUid())){
                    folders.get(i).setName(name);
                    return "Success";
                }
            }
        }
        while (folders!=null&&folders.size()>=0){
            for(int i=0;i<folders.size();i++){
                uid = folders.get(i).getUid();
                if(parentId.equals(uid)){
                    folders = folders.get(i).getFolders();
                    if(null == folders || folders.size()<=0){
                        return "Fail";
                    }
                    for(int j =0;j<folders.size();j++){
                        if(id.equals(folders.get(j).getUid())){
                            folders.get(i).setName(name);
                            return "Success";
                        }
                    }
                }
                folders = folders.get(i).getFolders();
            }
            if(parentId.equals(uid)){
                break;
            }
        }
        return "Fail";
    }

    private static String deleteFolder(FileStruct fileStruct,String parentId,String id){
        String uid = fileStruct.getUid();
        ArrayList<FileStruct> folders = fileStruct.getFolders();
        if(parentId.equals(uid)){
            if(null == folders || folders.size()<=0){
                return "Fail";
            }
            for(int i =0;i<folders.size();i++){
                if(id.equals(folders.get(i).getUid())){
                    folders.remove(i);
                    return "Success";
                }
            }
        }
        while (folders!=null&&folders.size()>=0){
            for(int i=0;i<folders.size();i++){
                uid = folders.get(i).getUid();
                if(parentId.equals(uid)){
                    folders = folders.get(i).getFolders();
                    if(null == folders || folders.size()<=0){
                        return "Fail";
                    }
                    for(int j =0;j<folders.size();j++){
                        if(id.equals(folders.get(j).getUid())){
                            folders.remove(j);
                            return "Success";
                        }
                    }
                }
                folders = folders.get(i).getFolders();
            }
            if(parentId.equals(uid)){
                break;
            }
        }
        return "Fail";
    }


    private static String createFolder(FileStruct fileStruct, String parentId, FileStruct folderStruct){
        String uid = fileStruct.getUid();
        ArrayList<FileStruct> folders = fileStruct.getFolders();
        if(parentId.equals(uid)){
            if(null == folders){
                folders = new ArrayList<>();
            }
            folders.add(folderStruct);
            return "Success";
        }
        while (folders!=null&&folders.size()>=0){
            for(int i=0;i<folders.size();i++){
                uid = folders.get(i).getUid();
                if(parentId.equals(uid)){
                    ArrayList<FileStruct> foldersChild = folders.get(i).getFolders();
                    if(null == foldersChild){
                        foldersChild = new ArrayList<>();
                    }
                    foldersChild.add(folderStruct);
                    folders.get(i).setFolders(foldersChild);
                    return "Success";
                }
                folders = folders.get(i).getFolders();
            }
            if(parentId.equals(uid)){
                break;
            }
        }
        return "Fail";
    }


    private static String addFileJson( FileStruct fileStruct,FileNode fileNode,String parentId){
        String uid = fileStruct.getUid();
        ArrayList<FileStruct> folders = fileStruct.getFolders();
        ArrayList<FileNode> files = fileStruct.getFiles();
        if(parentId.equals(uid)){
            if(null == files){
                files = new ArrayList<>();
            }
            files.add(fileNode);
            return "Success";
        }
        while (folders!=null&&folders.size()>=0){
            for(int i=0;i<folders.size();i++){
                uid = folders.get(i).getUid();
                if(parentId.equals(uid)){
                    files = folders.get(i).getFiles();
                    if(null == files){
                        files = new ArrayList<>();
                    }
                    files.add(fileNode);
                    folders.get(i).setFiles(files);
                    return "Success";
                }
                folders = folders.get(i).getFolders();
            }
            if(parentId.equals(uid)){
               break;
            }
        }
        return "Fail";

    }


}
