package org.example.roomiehub.service.impl;

import org.example.roomiehub.dto.response.UserPackageInfoResponse;
import org.example.roomiehub.exception.NoActivePackageException;
import org.example.roomiehub.model.UserPackage;
import org.example.roomiehub.repository.UserPackageRepository;
import org.example.roomiehub.service.UserService;

public class UserServiceImpl implements UserService {
    private  UserPackageRepository userPackageRepository;

    @Override
    public UserPackageInfoResponse getCurrentUserPackageInfo(Long userId) {
        UserPackage activePackage = userPackageRepository
                .findFirstByUserIdAndActiveTrueOrderByEndDateDesc(userId)
                .orElseThrow(() -> new NoActivePackageException("Bạn chưa có gói đăng bài hợp lệ"));

        return new UserPackageInfoResponse(
                activePackage.getPackageType(),
                activePackage.getRemainingPosts(),
                activePackage.isVrSupported(),
                activePackage.getEndDate()
        );
    }

}
