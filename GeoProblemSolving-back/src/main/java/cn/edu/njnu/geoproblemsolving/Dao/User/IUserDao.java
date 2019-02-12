package cn.edu.njnu.geoproblemsolving.Dao.User;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface IUserDao {

    String saveUser(UserEntity user);

    Object readUser(String key,String value);

    void removeUser(String key,String value);

    String updateUser(HttpServletRequest request);

    Object login(String email,String password);
}
