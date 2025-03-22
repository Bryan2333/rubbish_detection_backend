package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.User;
import com.bryan.rubbish_detection_backend.entity.dto.*;

public interface UserService extends IService<User> {
    User doLogin(LoginDTO loginDTO);

    PageResult<UserDTO> findByPageByAdmin(Integer pageNum, Integer pageSize, String username);

    Boolean createUser(RegistrationDTO registrationDTO);

    Boolean createUserByAdmin(AdminUserDTO adminUserDTO);

    UserDTO updateInfo(UserDTO userDTO);

    UserDTO updateInfoByAdmin(UserDTO userDTO);

    Boolean changePassword(ChangePasswordDTO changePasswordDTO);

    Boolean changeEmail(ChangeEmailDTO changeEmailDTO);

    Boolean resetPassword(ResetPasswordDTO resetPasswordDTO);
}
