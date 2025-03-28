package edu.scut.user_center.service;

import edu.scut.user_center.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author DS
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-03-20 16:31:39
*/
public interface UserService extends IService<User> {
    long userRegister(String userAccount, String userPassword, String confirmPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    Integer userLogout(HttpServletRequest request);

    User getSafetyUser(User originUser);

    User getCurrectUser(HttpServletRequest request);

    List<User> searchUsers(String username);

    Boolean deleteUser(Long id);
}
