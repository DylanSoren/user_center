package edu.scut.user_center.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DylanS
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 2494470058836843507L;

    private String userAccount;

    private String userPassword;

    private String confirmPassword;
}
