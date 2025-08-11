package org.example.roomiehub.service;

import org.example.roomiehub.dto.response.UserPackageInfoResponse;
import org.example.roomiehub.model.UserPackage;

public interface UserService {
    UserPackageInfoResponse getCurrentUserPackageInfo(Long userId);

}
