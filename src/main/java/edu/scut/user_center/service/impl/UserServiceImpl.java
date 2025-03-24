package edu.scut.user_center.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.scut.user_center.model.entity.User;
import edu.scut.user_center.service.UserService;
import edu.scut.user_center.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author DS
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-03-20 16:31:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param confirmPassword 确认密码
     * @return 用户ID
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String confirmPassword) {
        // 非空校验
        if (CharSequenceUtil.hasBlank(userAccount, userPassword, confirmPassword)) {
            return -1;
        }

        // 长度校验
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8) {
            return -1;
        }

        // 账号不能包含特殊字符
        boolean isMatch = ReUtil.isMatch(".*(\\pP|\\pS|\\s+).*", userAccount);
        if (isMatch) {
            return -1;
        }

        // 密码和确认密码要一致
        if (!userPassword.equals(confirmPassword)) {
            return -1;
        }

        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 密码加密
        final String SALT = "YSQ"; // 盐值
        String securePassword = SecureUtil.md5(SALT + userPassword);

        // 将数据插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(securePassword);
        boolean saved = this.save(user);
        if (!saved) {
            return -1;
        }

        return user.getId();
    }

    public static void main(String[] args) {
        String userAccount = "ysq666";
        boolean isMatch = ReUtil.isMatch(".*(\\pP|\\pS|\\s+).*", userAccount);
        if (isMatch) {
            System.out.println("error");
        } else {
            System.out.println("ok");
        }
    }
}



