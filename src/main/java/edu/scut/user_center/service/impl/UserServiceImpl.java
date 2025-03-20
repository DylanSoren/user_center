package edu.scut.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.scut.user_center.model.entity.User;
import edu.scut.user_center.service.UserService;
import edu.scut.user_center.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author DS
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-03-20 16:31:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
}




