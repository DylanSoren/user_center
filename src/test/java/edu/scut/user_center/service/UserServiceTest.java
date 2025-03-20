package edu.scut.user_center.service;

import edu.scut.user_center.model.entity.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 * @author DylanS
 * @version 1.0
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("YSQ");
        user.setUserAccount("123");
        user.setAvatarUrl("https://img-s-msn-com.akamaized.net/tenant/amp/entityid/BB1msP5m.img");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("16679017322");
        user.setEmail("49846749930@qq.com");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        assertTrue(save);
    }
}