package cn.edu.njnu.geoproblemsolving.Dao.Resource;

import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Dao.SubProject.SubProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
public class ResourceDaoImpl implements IResourceDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ResourceDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Object saveResource(HttpServletRequest request,String folderName) {
        ArrayList<ResourceUploadInfo> uploadInfos = new ArrayList<>();
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            String servicePath = request.getSession().getServletContext().getRealPath("/");
            if (!ServletFileUpload.isMultipartContent(request)) {
                System.out.println("File is not multimedia.");
                return "Fail";
            }
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String uploadTime = dateFormat.format(date);

            Collection<Part> parts = request.getParts();
            for (Part part : parts) {
                if (part.getName().equals("file")) {
                    if (part.getSize() < 1024 * 1024 * 1024) {
                        ResourceUploadInfo uploadInfo = new ResourceUploadInfo();
                        String fileNames = part.getSubmittedFileName();
                        String fileName = fileNames.substring(0, fileNames.lastIndexOf("."));
                        String suffix = fileNames.substring(fileNames.lastIndexOf(".") + 1);
                        String regexp = "[^A-Za-z_0-9\\u4E00-\\u9FA5]";
                        String saveName = fileName.replaceAll(regexp, "");
                        String folderPath = servicePath + "resource/"+folderName;
                        File temp = new File(folderPath);
                        if (!temp.exists()) {
                            temp.mkdirs();
                        }

                        int randomNum = (int) (Math.random() * 10 + 1);
                        for (int i = 0; i < 5; i++) {
                            randomNum = randomNum * 10 + (int) (Math.random() * 10 + 1);
                        }
                        String newFileTitle=saveName + randomNum + "." + suffix;
                        String localPath = temp + "/" + newFileTitle;
                        System.out.println("资源上传到本地路径："+localPath);
                        File file = new File(localPath);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        InputStream inputStream = part.getInputStream();
                        byte[] buffer = new byte[1024 * 1024];
                        int byteRead;
                        while ((byteRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, byteRead);
                        }
                        fileOutputStream.close();
                        inputStream.close();

                        String reqPath = request.getRequestURL().toString();
                        String pathURL = reqPath.replaceAll("localhost", ip) + "/" + newFileTitle;
                        String regexGetUrl="(/GeoProblemSolving[\\S]*)";
                        Pattern regexPattern=Pattern.compile(regexGetUrl);
                        Matcher matcher=regexPattern.matcher(pathURL);
                        if (matcher.find()){
                            pathURL=matcher.group(1);
                        }
                        uploadInfo.setFileName(newFileTitle);

                        // 如果是zip文件
                        if(suffix.equals("zip")){
                            try {
                                ArrayList<String> fileInZip = new ArrayList<>();
                                ZipFile zipFile = new ZipFile(localPath);
                                Enumeration enumeration = zipFile.entries();
                                while (enumeration.hasMoreElements()){
                                    ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
                                        String fn = zipEntry.getName();
                                        String[] nameArray = fn.split("\\\\");
                                        fn = nameArray[nameArray.length - 1];
                                        fileInZip.add(fn);
                                }
                                uploadInfo.setZipFiles(fileInZip);
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        String resourceId = UUID.randomUUID().toString();
                        uploadInfo.setResourceId(resourceId);
                        String fileSize;
                        DecimalFormat df = new DecimalFormat("##0.00");
                        if (part.getSize() > 1024 * 1024) {
                            fileSize = df.format((float) part.getSize() / (float) (1024 * 1024)) + "MB";
                        } else {
                            fileSize = df.format((float) part.getSize() / (float) (1024)) + "KB";
                        }
                        ResourceEntity resourceEntity = new ResourceEntity();
                        resourceEntity.setResourceId(resourceId);

                        JSONObject scope = JSON.parseObject(request.getParameter("scope"));
                        // decode projectId
                        String projectId = scope.getString("projectId");
                        if (projectId.length() > 36) {
                            String sid = new String(EncodeUtil.decode(projectId));
                            projectId = sid.substring(0, sid.length() - 2);
                        }
                        Query queryUser=Query.query(Criteria.where("userId").is(request.getParameter("uploaderId")));
                        UserEntity uploader=mongoTemplate.findOne(queryUser,UserEntity.class);
                        scope.put("projectId", projectId);

                        resourceEntity.setScope(scope);
                        resourceEntity.setName(fileNames);
                        resourceEntity.setDescription(request.getParameter("description"));
                        resourceEntity.setBelong(request.getParameter("belong"));
                        resourceEntity.setPrivacy(request.getParameter("privacy"));
                        resourceEntity.setType(request.getParameter("type"));
                        resourceEntity.setFileSize(fileSize);
                        resourceEntity.setPathURL(pathURL);
                        resourceEntity.setUploaderId(uploader.getUserId());
                        resourceEntity.setUploaderName(uploader.getUserName());
                        resourceEntity.setOrganization(uploader.getOrganization());
                        resourceEntity.setUploadTime(uploadTime);
                        mongoTemplate.save(resourceEntity);
                        uploadInfos.add(uploadInfo);

                    } else {
                        return "Size over";
                    }
                }
            }
            return uploadInfos;
        } catch (Exception e) {
            return uploadInfos;
        }
    }

    @Override
    public Object readResource(String key, String value) {
        try {
            // decode scopeId
            String sId = value;
            if (sId.length() > 36) {
                String scopeId = new String(EncodeUtil.decode(sId));
                value = scopeId.substring(0, scopeId.length() - 2);
            }
            Query query = new Query(Criteria.where(key).is(value));

            if (!mongoTemplate.find(query, ResourceEntity.class).isEmpty()) {

                // encode scopeId
                List<ResourceEntity> resourceEntitites = mongoTemplate.find(query, ResourceEntity.class);

                for(int i = 0;i < resourceEntitites.size();i++){
                    // get
                    ResourceEntity resourceEntitity = resourceEntitites.get(i);
                    JSONObject scope = resourceEntitity.getScope();
                    if(scope == null){
                        continue;
                    }

                    // encode
                    String projectId = scope.getString("projectId");
                    if(projectId.length() > 0) {
                        String randomID = UUID.randomUUID().toString().substring(0, 2);
                        projectId = EncodeUtil.encode((projectId + randomID).getBytes());
                    }
                    // set
                    scope.put("projectId",projectId);
                    resourceEntitity.setScope(scope);
                }

                return resourceEntitites;
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object readPublicResource(){
        try {
            Query query = Query.query(Criteria.where("privacy").is("public"));
            return mongoTemplate.find(query,ResourceEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteResource(String key, String value) {
        try {
            deleteFromSubProject(value);
            Query query = new Query(Criteria.where(key).is(value));
            mongoTemplate.remove(query, ResourceEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public void getZipResource(HttpServletRequest request, HttpServletResponse response, String zipName, String fileName) {
        response.setContentType("application/octet-stream");
        try {
            OutputStream outputStream = response.getOutputStream();
            String servicePath = request.getSession().getServletContext().getRealPath("/");
            String folderPath = servicePath + "resource/upload";
            File temp = new File(folderPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            String localPath = temp + "/" + zipName;
            ZipFile zipFile = new ZipFile(localPath);
            InputStream in = new BufferedInputStream(new FileInputStream(localPath));
            ZipInputStream zipInputStream = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze=zipInputStream.getNextEntry())!=null){
                if(!ze.isDirectory()){
                    if(ze.getName().contains(fileName)){
                        InputStream zeInputStream = zipFile.getInputStream(ze);
                        IOUtils.copy(zeInputStream,outputStream);
                        break;
                    }
                }
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object shareResource(HttpServletRequest request){
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String uploadTime = dateFormat.format(date);
            String resourceId=UUID.randomUUID().toString();
            ResourceEntity resourceEntity=new ResourceEntity();
            resourceEntity.setResourceId(resourceId);
            resourceEntity.setUploadTime(uploadTime);
            resourceEntity.setName(request.getParameter("name"));
            resourceEntity.setDescription(request.getParameter("description"));
            resourceEntity.setBelong(request.getParameter("belong"));
            resourceEntity.setType(request.getParameter("type"));
            resourceEntity.setFileSize(request.getParameter("fileSize"));
            resourceEntity.setPathURL(request.getParameter("pathURL"));
            resourceEntity.setUploaderId(request.getParameter("uploaderId"));
            JSONObject scope = JSON.parseObject(request.getParameter("scope"));
            // decode projectId
            String projectId = scope.getString("projectId");
            if (projectId.length() > 36) {
                String sid = new String(EncodeUtil.decode(projectId));
                projectId = sid.substring(0, sid.length() - 2);
            }
            scope.put("projectId", projectId);

            resourceEntity.setScope(scope);

            mongoTemplate.save(resourceEntity);
            Query query=new Query(Criteria.where("resourceId").is(resourceId));
            return mongoTemplate.findOne(query,ResourceEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    public void packageToZip(HttpServletRequest request,HttpServletResponse response){
        String fileURLsStr = request.getParameter("fileURLs");
        String[] fileURLs = fileURLsStr.split(",");
        List files = new ArrayList();
        for (String fileURL : fileURLs) {
            String regexGetUrl="/GeoProblemSolving/([\\S]*)";
            Pattern regexPattern=Pattern.compile(regexGetUrl);
            Matcher matcher=regexPattern.matcher(fileURL);
            if (matcher.find()){
                fileURL=matcher.group(1);
            }
//            fileURL = fileURL.replace("/","\\");
            String localPath =request.getSession().getServletContext().getRealPath("/") + fileURL;
            File file = new File(localPath);
            files.add(file);
        }
        try {
            String servicePath = request.getSession().getServletContext().getRealPath("/");
            String folderPath = servicePath+"resource/tempZIP/";
            File tempFolder = new File(folderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }
            String tempZIP = folderPath+ UUID.randomUUID()+".zip";
            File zipFile = new File(tempZIP);
            zipFile.createNewFile();
            response.reset();
            FileOutputStream fOut = new FileOutputStream(tempZIP);
            ZipOutputStream zipOut = new ZipOutputStream(fOut);
            for (Object file : files) {
                zipFile((File) file, zipOut);
            }
            zipOut.close();
            fOut.close();
            downloadZip(zipFile,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //删除时改变子项目的文件树结构
    private void deleteFromSubProject(String resourceId){
        try {
            Query queryResource = Query.query(Criteria.where("resourceId").is(resourceId));
            ResourceEntity resourceEntity = mongoTemplate.findOne(queryResource,ResourceEntity.class);
            JSONObject scope = resourceEntity.getScope();
            String subProjectId = scope.getString("subProjectId");
            if(!subProjectId.equals("")){
                Query querySubProject = Query.query(Criteria.where("subProjectId").is(subProjectId));
                SubProjectEntity subProjectEntity = mongoTemplate.findOne(querySubProject,SubProjectEntity.class);
                String folderStruct = subProjectEntity.getFileStruct();
                FileStruct fileStruct = JSONObject.parseObject(folderStruct,FileStruct.class);
                String parentId = findParentId(fileStruct,resourceId);
                SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
                String res=subProjectDao.deleteFile(subProjectId,parentId,resourceId);
                if(!res.equals("Fail")){
                    subProjectDao.updateFileStruct(subProjectId,res);
                }
            }
        }catch (Exception ignored){}
    }

    private String findParentId(FileStruct folderStruct, String resourceId){
        ArrayList<FileStruct> fileStructs = folderStruct.getFolders();
        ArrayList<FileNode> fileNodes = folderStruct.getFiles();
        for(FileNode fileNode:fileNodes){
            if (fileNode.getUid().equals(resourceId)){
                return folderStruct.getUid();
            }
        }
        for (FileStruct fileStruct:fileStructs){
            String parentId =  findParentId(fileStruct,resourceId);
            if(!parentId.equals("")){
                return parentId;
            }
        }
        return "";
    }

    private static void zipFile(File inputFile, ZipOutputStream outputStream){
        try {
            if (inputFile.exists()){
                if (inputFile.isFile()){
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN,512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    outputStream.putNextEntry(entry);
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer))!=-1){
                        outputStream.write(buffer,0,nNumber);
                    }
                    bins.close();
                    IN.close();
                }
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    private static void downloadZip(File file,HttpServletResponse response){
        if (!file.exists()){
            System.out.println("待压缩目录"+file+"不存在");
        }
        else {
            try {
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                response.reset();
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    File temp = new File(file.getPath());
                    temp.delete();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}