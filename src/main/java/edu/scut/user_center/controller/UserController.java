package edu.scut.user_center.controller;

import cn.hutool.core.text.CharSequenceUtil;
import edu.scut.user_center.model.entity.User;
import edu.scut.user_center.model.request.UserLoginRequest;
import edu.scut.user_center.model.request.UserRegisterRequest;
import edu.scut.user_center.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
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
    public User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
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
}
