package com.tp.dto.mmp.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ldr on 2016/1/7.
 */
@Deprecated
public enum Platform {

    PC(1, 1),

    ANDROID(2, 1 << 1),

    IOS(3, 1 << 2),

    WAP(4, 1 << 3),

    WX(5, 1 << 4);

    private int code;

    private int value;

    Platform(int code, int value) {
        this.code = code;
        this.value = value;
    }

    public static Integer ALL() {
        int i = 0;
        for (Platform platform : Platform.values()) {
            i += platform.value;
        }
        return i;
    }

    public static Integer APP() {
        return IOS.value + ANDROID.value;
    }

    public static List<Integer> getValuesByValue(Integer value) {
        if (value == null) {
            return Collections.emptyList();
        }
        List<Integer> platforms = new ArrayList<>();
        for (Platform platform : Platform.values()) {
            if ((platform.value & value) == platform.value) {
                platforms.add(platform.value);
            }
        }
        return platforms;
    }

    public static Platform getByValue(Integer value) {
        if (value == null) return null;
        for (Platform platform : Platform.values()) {
            if (platform.value == value) {
                return platform;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }
}
