package cn.edu.njnu.geoproblemsolving.Dao.Project;

import cn.edu.njnu.geoproblemsolving.Dao.Email.EmailDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Dao.User.UserDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProjectDaoImpl implements IProjectDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProjectDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String createProject(ProjectEntity project) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String projectId = UUID.randomUUID().toString();
        project.setProjectId(projectId);
        JSONArray members = new JSONArray();
        project.setMembers(members);
        project.setCreateTime(dateFormat.format(date));
        project.setEndTime("");
        Query query = Query.query(Criteria.where("userId").is(project.getManagerId()));
        UserEntity userInfo = mongoTemplate.findOne(query, UserEntity.class);
        project.setManagerName(userInfo.getUserName());
        JSONArray manageProjects = userInfo.getManageProjects();
        JSONObject projectInfo = new JSONObject();
        projectInfo.put("projectId", project.getProjectId());
        projectInfo.put("title", project.getTitle());
        manageProjects.add(projectInfo);
        Update updateUser = new Update();
        updateUser.set("manageProjects", manageProjects);
        mongoTemplate.updateFirst(query, updateUser, UserEntity.class);
        mongoTemplate.save(project);
        // encode
        if (projectId.length() == 36) {
            String randomID = UUID.randomUUID().toString().substring(0, 2);
            projectId = EncodeUtil.encode((projectId + randomID).getBytes());
        }
        return projectId;
    }

    @Override
    public Object readAllProjects() {
        try {
            Criteria criteria1 = Criteria.where("privacy").is("Discoverable");
            Criteria criteria2 = Criteria.where("privacy").is("Public");
            Query queryProjects = Query.query(new Criteria().orOperator(criteria1, criteria2));
            List<ProjectEntity> projectEntityList = mongoTemplate.find(queryProjects, ProjectEntity.class);
            if (!projectEntityList.isEmpty()) {
                projectEntityList = encodeProjetId(projectEntityList);
                return projectEntityList;
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object readByType(String key, String value) {
        try {
            Query queryProjects = new Query();
            Criteria criteria = new Criteria();
            criteria.andOperator(Criteria.where("privacy").is("Public"), Criteria.where(key).is(value));
            queryProjects.addCriteria(criteria);
            List<ProjectEntity> projectEntityList = mongoTemplate.find(queryProjects, ProjectEntity.class);
            if (!projectEntityList.isEmpty()){
                projectEntityList = encodeProjetId(projectEntityList);
                return projectEntityList;
            }else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object readProject(String key, String value) {
        if (key.equals("projectId")) {
            // decode
            String pid = value;
            if (pid.length() > 36) {
                String projectId = new String(EncodeUtil.decode(pid));
                value = projectId.substring(0, projectId.length() - 2);
            }
        }
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, ProjectEntity.class).isEmpty()) {
            return "None";
        } else {
            List<ProjectEntity> projectEntities = mongoTemplate.find(query, ProjectEntity.class);
            projectEntities = encodeProjetId(projectEntities);
            return projectEntities;
        }
    }

    @Override
    public void deleteProject(String key, String value) {

        // decode
        String pid = value;
        if (pid.length() > 36) {
            String projectId = new String(EncodeUtil.decode(pid));
            value = projectId.substring(0, projectId.length() - 2);
        }
        Query query = Query.query(Criteria.where(key).is(value));
        ProjectEntity projectEntity = mongoTemplate.findOne(query, ProjectEntity.class);
        JSONArray members = projectEntity.getMembers();
        for (int i = 0; i < members.size(); i++) {
            JSONObject memberObject = members.getJSONObject(i);
            quitProject(value, memberObject.getString("userId"));
        }
        String managerId = projectEntity.getManagerId();
        Query queryManager = Query.query(Criteria.where("userId").is(managerId));
        UserEntity managerObject = mongoTemplate.findOne(queryManager, UserEntity.class);
        JSONArray manageProjects = managerObject.getManageProjects();
        for (int i = 0; i < manageProjects.size(); i++) {
            JSONObject manageProject = manageProjects.getJSONObject(i);
            if (manageProject.get("projectId").equals(value)) {
                manageProjects.remove(i);
                break;
            }
        }
        Update updateManager = new Update();
        updateManager.set("manageProjects", manageProjects);
        mongoTemplate.updateFirst(queryManager, updateManager, UserEntity.class);
        mongoTemplate.remove(query, "Project");
    }

    @Override
    public Object updateProject(HttpServletRequest request) {
        try {
            // decode
            String projectId = request.getParameter("projectId");
            if (projectId.length() > 36) {
                String pid = new String(EncodeUtil.decode(projectId));
                projectId = pid.substring(0, pid.length() - 2);
            }

            Query query = new Query(Criteria.where("projectId").is(projectId));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            update.set("projectId", projectId);
            mongoTemplate.updateFirst(query, update, ProjectEntity.class);
            ProjectEntity projectEntity = mongoTemplate.findOne(query, ProjectEntity.class);
            try {
                String newTitle = request.getParameter("title");

                Query queryManager = Query.query(Criteria.where("userId").is(projectEntity.getManagerId()));
                UserEntity manager = mongoTemplate.findOne(queryManager,UserEntity.class);
                JSONArray manageProjects = manager.getManageProjects();
                for (int i=0;i<manageProjects.size();i++){
                    JSONObject manageProject = manageProjects.getJSONObject(i);
                    if(manageProject.getString("projectId").equals(projectId)){
                        manageProject.put("title",newTitle);
                        manageProjects.remove(i);
                        manageProjects.add(i,manageProject);
                        Update updateUser = new Update();
                        updateUser.set("manageProjects",manageProjects);
                        mongoTemplate.updateFirst(queryManager,updateUser,UserEntity.class);
                        break;
                    }
                }

                JSONArray members = projectEntity.getMembers();
                for (int i=0;i<members.size();i++){
                    JSONObject member = members.getJSONObject(i);
                    String memberId = member.getString("userId");
                    Query queryUser = Query.query(Criteria.where("userId").is(memberId));
                    UserEntity memberObject = mongoTemplate.findOne(queryUser,UserEntity.class);
                    JSONArray joinedProjects = memberObject.getJoinedProjects();
                    for (int j=0;j<joinedProjects.size();j++){
                        JSONObject joinedProject = joinedProjects.getJSONObject(j);
                        if (joinedProject.getString("projectId").equals(projectId)){
                            joinedProject.put("title",newTitle);
                            joinedProjects.remove(j);
                            joinedProjects.add(j,joinedProject);
                            Update updateUser = new Update();
                            updateUser.set("joinedProjects",joinedProjects);
                            mongoTemplate.updateFirst(queryUser,updateUser,UserEntity.class);
                            break;
                        }
                    }
                }
            }catch (Exception ignored){}
            return projectEntity;
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object joinProject(String projectId, String userId) {
        try {
            // decode
            if (projectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(projectId));
                projectId = pId.substring(0, pId.length() - 2);
            }

            Query queryProject = new Query(Criteria.where("projectId").is(projectId));
            if (!mongoTemplate.find(queryProject, ProjectEntity.class).isEmpty()) {
                ProjectEntity project = mongoTemplate.findOne(queryProject, ProjectEntity.class);
                String managerId = project.getManagerId();
                JSONArray members = project.getMembers();
                Query queryUser = Query.query(Criteria.where("userId").is(userId));
                UserEntity user = mongoTemplate.findOne(queryUser, UserEntity.class);
                CommonMethod method = new CommonMethod();
                Object result = method.joinGroup(members, managerId, userId, user.getUserName());
                if (result.equals("Exist")) {
                    return "Exist";
                } else {
                    Update update = new Update();
                    update.set("members", result);
                    mongoTemplate.updateFirst(queryProject, update, ProjectEntity.class);
                    JSONArray joinedProjects = user.getJoinedProjects();
                    JSONObject newProjectInfo = new JSONObject();
                    newProjectInfo.put("projectId", project.getProjectId());
                    newProjectInfo.put("title", project.getTitle());
                    joinedProjects.add(newProjectInfo);
                    Update updateUser = new Update();
                    updateUser.set("joinedProjects", joinedProjects);
                    mongoTemplate.updateFirst(queryUser, updateUser, UserEntity.class);
                    return "Success";
                }
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String quitProject(String projectId, String userId) {
        try {
            // decode
            if (projectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(projectId));
                projectId = pId.substring(0, pId.length() - 2);
            }

            Query queryProject = new Query(Criteria.where("projectId").is(projectId));
            if (!mongoTemplate.find(queryProject, ProjectEntity.class).isEmpty()) {
                ProjectEntity project = mongoTemplate.findOne(queryProject, ProjectEntity.class);
                JSONArray members = project.getMembers();
                CommonMethod method = new CommonMethod();
                JSONArray newMembers = method.quitGroup(members, userId, "userId");
                Update updateProject = new Update();
                updateProject.set("members", newMembers);
                mongoTemplate.updateFirst(queryProject, updateProject, ProjectEntity.class);

                Query queryUser = new Query(Criteria.where("userId").is(userId));
                UserEntity user = mongoTemplate.findOne(queryUser, UserEntity.class);
                JSONArray joinedProjects = user.getJoinedProjects();
                JSONArray newJoinedProjects = method.quitGroup(joinedProjects, projectId, "projectId");
                Update updateUser = new Update();
                updateUser.set("joinedProjects", newJoinedProjects);
                mongoTemplate.updateFirst(queryUser, updateUser, UserEntity.class);

                quitSubProjectFromProject(projectId, userId);
                return "Success";
            } else {
                return "None";
            }

        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object changeManager(String projectId, String userId) {
        try {
            // decode
            if (projectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(projectId));
                projectId = pId.substring(0, pId.length() - 2);
            }

            Query query = new Query(Criteria.where("projectId").is(projectId));
            ProjectEntity project = mongoTemplate.findOne(query, ProjectEntity.class);
            quitProject(projectId, userId);
            JSONArray members = project.getMembers();
            CommonMethod method = new CommonMethod();
            JSONArray newMembers = method.quitGroup(members, userId, "userId");
            JSONObject foreManager = new JSONObject();
            foreManager.put("userId", project.getManagerId());
            foreManager.put("userName", project.getManagerName());
            newMembers.add(foreManager);
            Query oldManagerQuery = Query.query(Criteria.where("userId").is(project.getManagerId()));
            UserEntity oldManagerObject = mongoTemplate.findOne(oldManagerQuery, UserEntity.class);
            JSONArray oldManageProjects = oldManagerObject.getManageProjects();
            for (int i = 0; i < oldManageProjects.size(); i++) {
                JSONObject oldManageProject = oldManageProjects.getJSONObject(i);
                if (oldManageProject.get("projectId").equals(projectId)) {
                    oldManageProjects.remove(i);
                    break;
                }
            }
            Update updateOldManager = new Update();
            updateOldManager.set("manageProjects", oldManageProjects);
            Query newManagerQuery = Query.query(Criteria.where("userId").is(userId));
            UserEntity newManager = mongoTemplate.findOne(newManagerQuery, UserEntity.class);
            JSONArray newManageProjects = newManager.getManageProjects();
            JSONObject newManageProject = new JSONObject();
            newManageProject.put("projectId", project.getProjectId());
            newManageProject.put("title", project.getTitle());
            newManageProjects.add(newManageProject);
            JSONArray oldManageJoinedProjects = oldManagerObject.getJoinedProjects();
            oldManageJoinedProjects.add(newManageProject);
            updateOldManager.set("joinedProjects", oldManageJoinedProjects);
            mongoTemplate.updateFirst(oldManagerQuery, updateOldManager, UserEntity.class);
            Update updateNewManager = new Update();
            updateNewManager.set("manageProjects", newManageProjects);
            mongoTemplate.updateFirst(newManagerQuery, updateNewManager, UserEntity.class);
            Update update = new Update();
            update.set("managerId", newManager.getUserId());
            update.set("managerName", newManager.getUserName());
            update.set("members", newMembers);
            mongoTemplate.updateFirst(query, update, ProjectEntity.class);
            return mongoTemplate.findOne(query, ProjectEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String joinByMail(String projectId, String email, String password) {
        try {
            // decode
            if (projectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(projectId));
                projectId = pId.substring(0, pId.length() - 2);
            }
            UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
            UserEntity userEntity = new UserEntity();
            if (userDao.isRegistered(email)) {
                if (userDao.verifyPassword(email, password)) {
                    Query query = new Query(Criteria.where("email").is(email));
                    userEntity = mongoTemplate.findOne(query, UserEntity.class);
                } else {
                    return "Password";
                }
            } else {
                userEntity.setUserId(UUID.randomUUID().toString());
                userEntity.setUserName(email);
                userEntity.setEmail(email);
                userEntity.setPassword(password);
                userEntity.setMobilePhone("");
                userEntity.setJobTitle("");
                userEntity.setJoinedProjects(new JSONArray());
                userEntity.setManageProjects(new JSONArray());
                userEntity.setAvatar("");
                userEntity.setCity("");
                userEntity.setCountry("");
                userEntity.setDirection("");
                userEntity.setGender("");
                userEntity.setHomePage("");
                userEntity.setIntroduction("");
                userDao.saveUser(userEntity);
            }
            String userId = userEntity.getUserId();
            String result = joinProject(projectId, userId).toString();
            if (!result.equals("Success")) {
                return result;
            }
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String applyByEmail(EmailEntity emailEntity) {
        try {
            String userId = emailEntity.getRecipient();
            Query queryUser = Query.query(Criteria.where("userId").is(userId));
            UserEntity manager = mongoTemplate.findOne(queryUser, UserEntity.class);
            emailEntity.setRecipient(manager.getEmail());
            EmailDaoImpl emailDao = new EmailDaoImpl();
            emailDao.sendEmail(emailEntity);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String uploadPicture(HttpServletRequest request) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            String servicePath = request.getSession().getServletContext().getRealPath("/");
            if (!ServletFileUpload.isMultipartContent(request)) {
                System.out.println("File is not multimedia.");
                return "Fail";
            }
            Collection<Part> parts = request.getParts();
            String pathURL = "Fail";
            for (Part part : parts) {
                if (part.getName().equals("picture")) {
                    String fileNames = part.getSubmittedFileName();
                    String fileName = fileNames.substring(0, fileNames.lastIndexOf("."));
                    String suffix = fileNames.substring(fileNames.lastIndexOf(".") + 1);
                    String regexp = "[^A-Za-z_0-9\\u4E00-\\u9FA5]";
                    String saveName = fileName.replaceAll(regexp, "");
                    String folderPath = servicePath + "project\\picture";
                    File temp = new File(folderPath);
                    if (!temp.exists()) {
                        temp.mkdirs();
                    }
                    int randomNum = (int) (Math.random() * 10 + 1);
                    for (int i = 0; i < 5; i++) {
                        randomNum = randomNum * 10 + (int) (Math.random() * 10 + 1);
                    }
                    String newFileTitle = saveName + randomNum + "." + suffix;
                    String localPath = temp + "\\" + newFileTitle;
                    System.out.println("图片上传到本地路径：" + localPath);
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
                    pathURL = reqPath.replaceAll("localhost", ip) + "/" + newFileTitle;
                    String regexGetUrl = "(/GeoProblemSolving[\\S]*)";
                    Pattern regexPattern = Pattern.compile(regexGetUrl);
                    Matcher matcher = regexPattern.matcher(pathURL);
                    if (matcher.find()) {
                        pathURL = matcher.group(1);
                    }
//                    System.out.println("图片的请求地址："+pathURL);
                    break;
                }
            }
            return pathURL;
        } catch (Exception e) {
            return "Fail";
        }
    }

    private void quitSubProjectFromProject(String projectId, String userId) {
        Query querySubProjects = Query.query(Criteria.where("projectId").is(projectId));
        List<SubProjectEntity> subProjectList = mongoTemplate.find(querySubProjects, SubProjectEntity.class);
        for (SubProjectEntity subProject : subProjectList) {
            Query query = Query.query(Criteria.where("subProjectId").is(subProject.getSubProjectId()));
            JSONArray members = subProject.getMembers();
            String managerId = subProject.getManagerId();
            if (managerId.equals(userId)) {
                if (members.size() > 0) {
                    JSONObject firstMember = members.getJSONObject(0);
                    members.remove(0);
                    Update update = new Update();
                    update.set("managerId", firstMember.get("userId"));
                    update.set("managerName", firstMember.get("userName"));
                    update.set("members", members);
                    mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
                } else {
                    mongoTemplate.remove(query, SubProjectEntity.class);
                }
            } else {
                for (int i = 0; i < members.size(); i++) {
                    JSONObject member = members.getJSONObject(i);
                    if (member.getString("userId").equals(userId)) {
                        members.remove(i);
                        Update update = new Update();
                        update.set("members", member);
                        mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
                        break;
                    }
                }
            }
        }
    }

    private List<ProjectEntity> encodeProjetId(List<ProjectEntity> projectEntityList) {

        for (ProjectEntity projectEntity : projectEntityList) {
            // get
            String projectId = projectEntity.getProjectId();
            // encode
            if (projectId.length() == 36) {
                String randomID = UUID.randomUUID().toString().substring(0, 2);
                projectId = EncodeUtil.encode((projectId + randomID).getBytes());
            }
            // set
            projectEntity.setProjectId(projectId);
        }
        return projectEntityList;
    }
}
