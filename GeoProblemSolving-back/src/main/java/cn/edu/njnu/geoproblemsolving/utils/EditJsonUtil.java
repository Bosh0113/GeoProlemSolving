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
        }else{
            for(int i=0;i<folders.size();i++){
                String res = deleteFile(folders.get(i),parentId,id);
                if(res == "Success"){
                    return res;
                }
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
        }else{
            for(int i=0;i<folders.size();i++){
                String res = renameFolder(folders.get(i),parentId,id,name);
                if(res == "Success"){
                    return res;
                }
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
        }else{
            for(int i=0;i<folders.size();i++){
                String res = deleteFolder(folders.get(i),parentId,id);
                if(res == "Success"){
                    return res;
                }
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
        }else{
            for(int i=0;i<folders.size();i++){
                String res = createFolder(folders.get(i),parentId,folderStruct);
                if(res == "Success"){
                    return res;
                }
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
        }else{
            for(int i=0;i<folders.size();i++){
                String res = addFileJson(folders.get(i),fileNode,parentId);
                if(res == "Success"){
                    return res;
                }
            }
        }
        return "Fail";
    }


}
