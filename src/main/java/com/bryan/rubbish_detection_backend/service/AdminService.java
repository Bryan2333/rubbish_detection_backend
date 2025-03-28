package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.Admin;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.dto.LoginDTO;

public interface AdminService extends IService<Admin> {
    Admin doLogin(LoginDTO dto);

    PageResult<Admin> findByPageByAdmin(Integer pageNum, Integer pageSize, String username);

    Boolean createAdmin(Admin admin);

    Admin updateAdmin(Admin admin, Boolean currentOperationIsSuper);

    Boolean updatePassword(Long adminId, String oldPassword, String newPassword, String confirmPassword);
}
