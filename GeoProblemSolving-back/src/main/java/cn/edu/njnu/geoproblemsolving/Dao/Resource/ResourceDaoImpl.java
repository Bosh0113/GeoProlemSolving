package cn.edu.njnu.geoproblemsolving.Dao.Resource;

import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ResourceDaoImpl implements IResourceDao{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ResourceDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveResource(HttpServletRequest request){
        try {
            InetAddress address=InetAddress.getLocalHost();
            String ip=address.getHostAddress();
            String servicePath=request.getSession().getServletContext().getRealPath("/");
            if(!ServletFileUpload.isMultipartContent(request)){
                System.out.println("File is not multimedia.");
                return "Fail";
            }
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String uploadTime=dateFormat.format(date);

            Collection<Part> parts=request.getParts();
            for(Part part:parts){
                if (part.getName().equals("file")){
                    if (part.getSize()<100*1024*1024){
                        String fileNames=part.getSubmittedFileName();
                        String[] fileInfo=fileNames.split("\\.");
                        String fileName=fileInfo[0];
                        String suffix=fileInfo[1];
                        String regexp="[^A-Za-z_0-9\\u4E00-\\u9FA5]";
                        String saveName=fileName.replaceAll(regexp,"");
                        String folderPath=servicePath+"resource\\upload";
                        File temp=new File(folderPath);
                        if (!temp.exists()){
                            temp.mkdirs();
                        }

                        int randomNum=(int)(Math.random()*10+1);
                        for (int i=0;i<5;i++){
                            randomNum=randomNum*10+(int)(Math.random()*10+1);
                        }

                        String localPath=temp+"\\"+saveName+randomNum+"."+suffix;
                        System.out.println("localPath: "+localPath);

                        File file=new File(localPath);
                        FileOutputStream fileOutputStream=new FileOutputStream(file);
                        InputStream inputStream=part.getInputStream();
                        byte[] buffer=new byte[1024*1024];
                        int byteRead;
                        while ((byteRead=inputStream.read(buffer))!=-1){
                            fileOutputStream.write(buffer,0,byteRead);
                        }
                        fileOutputStream.close();
                        inputStream.close();

                        String reqPath=request.getRequestURL().toString();
                        String pathURL=reqPath.replaceAll("localhost",ip)+"/"+saveName+randomNum+"."+suffix;
                        System.out.println("downloadURL: "+pathURL);

                        String resourceId=UUID.randomUUID().toString();
                        String fileSize;
                        DecimalFormat df=new DecimalFormat("##0.00");
                        if (part.getSize()>1024*1024){
                            fileSize=df.format((float)part.getSize()/(float) (1024*1024))+"MB";
                        }
                        else {
                            fileSize=df.format((float)part.getSize()/(float)(1024))+"KB";
                        }
                        ResourceEntity resourceEntity=new ResourceEntity();
                        resourceEntity.setResourceId(resourceId);

                        // decode scopeId
                        String scopeId = request.getParameter("scopeId");
                        if(scopeId.length() > 36) {
                            String sid = new String(EncodeUtil.decode(scopeId));
                            scopeId = sid.substring(0, sid.length() - 2);
                        }

                        resourceEntity.setScopeId(scopeId);
                        resourceEntity.setName(fileNames);
                        resourceEntity.setDescription(request.getParameter("description"));
                        resourceEntity.setType(request.getParameter("type"));
                        resourceEntity.setFileSize(fileSize);
                        resourceEntity.setPathURL(pathURL);
                        resourceEntity.setUploaderId(request.getParameter("uploaderId"));
                        resourceEntity.setUploadTime(uploadTime);
                        mongoTemplate.save(resourceEntity);
                    }
                    else {
                        return "Size over";
                    }
                }
            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object readResource(String key,String value){
        try {
            // decode scopeId
            String sId = value;
            if(sId.length() > 36) {
                String scopeId = new String(EncodeUtil.decode(sId));
                value = scopeId.substring(0, scopeId.length() - 2);
            }
            Query query=new Query(Criteria.where(key).is(value));

            if (!mongoTemplate.find(query,ResourceEntity.class).isEmpty()){

                // encode scopeId
                List<ResourceEntity> resourceEntitites = mongoTemplate.find(query,ResourceEntity.class);

//                for(int i = 0;i < resourceEntitites.size();i++){
//                    // get
//                    ResourceEntity resourceEntitity = resourceEntitites.get(i);
//                    String scopeId = resourceEntitity.getScopeId();
//                    // encode
//                    if(scopeId.length() == 36) {
//                        String randomID = UUID.randomUUID().toString().substring(0, 2);
//                        scopeId = EncodeUtil.encode((scopeId + randomID).getBytes());
//                    }
//                    // set
//                    resourceEntitity.setScopeId(scopeId);
//                }

                return resourceEntitites;
            }else {
                return "None";
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteResource(String key,String value){
        try {
            Query query=new Query(Criteria.where(key).is(value));
            mongoTemplate.remove(query,ResourceEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
