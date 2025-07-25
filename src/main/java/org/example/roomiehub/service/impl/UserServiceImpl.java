package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.response.UserPackageInfoResponse;
import org.example.roomiehub.exception.NoActivePackageException;
import org.example.roomiehub.model.UserPackage;
import org.example.roomiehub.repository.UserPackageRepository;
import org.example.roomiehub.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserPackageRepository userPackageRepository;

    @Override
    public UserPackageInfoResponse getCurrentUserPackageInfo(Long userId) {
        UserPackage activePackage = userPackageRepository
                .findFirstByUserIdAndActiveTrueOrderByEndDateDesc(userId)
                .orElse(null);

        return new UserPackageInfoResponse(
                activePackage.getPackageType(),
                activePackage.getRemainingPosts(),
                activePackage.isVrSupported(),
                activePackage.getEndDate()
        );
    }

}
