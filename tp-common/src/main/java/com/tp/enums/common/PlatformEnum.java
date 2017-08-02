package com.tp.enums.common;

import java.util.ArrayList;
import java.util.List;

import com.tp.common.vo.ord.BaseEnum;

public enum PlatformEnum implements BaseEnum {

    ALL(1, "全平台", (1<<10)-1),

    PC(2, "PC网页端", 1 << 1),

    WAP(3, "手机网页端", 1 << 2),

    IOS(4, "IOS客户端", 1 << 3),

    ANDROID(5, "ANDROID客户端", 1 << 4),

    WX(6, "微信", 1 << 5);

    public int code;

    public String desc;

    public Integer binary;

    PlatformEnum(int code, String desc, Integer binary) {
        this.code = code;
        this.desc = desc;
        this.binary = binary;
    }

    public static PlatformEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            if (platformEnum.code == code) {
                return platformEnum;
            }
        }
        return null;
    }

    public static Integer getCodeByName(String name) {
        if (name == null) return null;
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            if (platformEnum.name().equalsIgnoreCase(name)) return platformEnum.getCode();
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        PlatformEnum platformEnum = getByCode(code);
        return platformEnum == null ? "" : platformEnum.getDesc();
    }

    public boolean contains(int code) {
        for (PlatformEnum entry : PlatformEnum.values()) {
            if (entry.getCode() == code) {
                return true;
            }
        }
        return false;
    }
    public static List<Integer> getAllCode(){
        List<Integer> platforms = new ArrayList<>();
        for(PlatformEnum platformEnum:PlatformEnum.values()){
            platforms.add(platformEnum.getCode());
        }
        return platforms;
    }


    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public int getBinary() {
        return binary;
    }
}
