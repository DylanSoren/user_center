package edu.scut.user_center.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DylanS
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -8765050689861040016L;

    private String userAccount;

    private String userPassword;
}
