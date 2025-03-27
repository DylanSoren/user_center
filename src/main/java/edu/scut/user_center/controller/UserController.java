package edu.scut.user_center.controller;

import cn.hutool.core.text.CharSequenceUtil;
import edu.scut.user_center.model.entity.User;
import edu.scut.user_center.model.request.UserLoginRequest;
import edu.scut.user_center.model.request.UserRegisterRequest;
import edu.scut.user_center.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static edu.scut.user_center.constant.UserConstant.ADMIN_ROLE;
import static edu.scut.user_center.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author DylanS
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String confirmPassword = userRegisterRequest.getConfirmPassword();
        if (CharSequenceUtil.hasBlank(userAccount, userPassword, confirmPassword)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, confirmPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (CharSequenceUtil.hasBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        if (isNotAdmin(request)) {
            return new ArrayList<>();
        }
        return userService.searchUsers(username);
    }

    @PostMapping("/delete")
    public Boolean deleteUser(@RequestBody Long id, HttpServletRequest request) {
        if (isNotAdmin(request)) {
            return false;
        }
        return userService.deleteUser(id);
    }

    /**
     * 是否为管理员
     * @param request HTTP请求
     * @return 判断结果
     */
    private static boolean isNotAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user == null || user.getUserRole() != ADMIN_ROLE;
    }
}