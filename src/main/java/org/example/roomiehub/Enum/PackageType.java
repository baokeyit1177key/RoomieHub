package org.example.roomiehub.Enum;

import lombok.Getter;

@Getter
public enum PackageType {
    BASIC(10000),
    PROFESSIONAL(100000),
    VIP(150000),
    VR(200000);

    private final int price;

    PackageType(int price) {
        this.price = price;
    }

}
