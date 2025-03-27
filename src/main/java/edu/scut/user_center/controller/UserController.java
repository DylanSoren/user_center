package edu.scut.user_center.controller;

import cn.hutool.core.text.CharSequenceUtil;
import edu.scut.user_center.common.BaseResponse;
import edu.scut.user_center.common.ResultUtils;
import edu.scut.user_center.exception.ThrowUtils;
import edu.scut.user_center.model.entity.User;
import edu.scut.user_center.model.request.UserLoginRequest;
import edu.scut.user_center.model.request.UserRegisterRequest;
import edu.scut.user_center.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.scut.user_center.common.StatusCode.NO_AUTH_ERROR;
import static edu.scut.user_center.common.StatusCode.PARAMS_ERROR;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, PARAMS_ERROR);

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String confirmPassword = userRegisterRequest.getConfirmPassword();
        ThrowUtils.throwIf(CharSequenceUtil.hasBlank(userAccount, userPassword, confirmPassword),
                PARAMS_ERROR);

        long id = userService.userRegister(userAccount, userPassword, confirmPassword);
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null || request == null, PARAMS_ERROR);

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        ThrowUtils.throwIf(CharSequenceUtil.hasBlank(userAccount, userPassword), PARAMS_ERROR);

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, PARAMS_ERROR);
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrectUser(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, PARAMS_ERROR);
        User currectUser = userService.getCurrectUser(request);
        return ResultUtils.success(currectUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        ThrowUtils.throwIf(isNotAdmin(request), NO_AUTH_ERROR);
        List<User> users = userService.searchUsers(username);
        return ResultUtils.success(users);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(isNotAdmin(request), NO_AUTH_ERROR);
        Boolean result = userService.deleteUser(id);
        return ResultUtils.success(result);
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