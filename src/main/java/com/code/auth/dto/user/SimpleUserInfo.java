package com.code.auth.dto.user;

import lombok.Data;

@Data
public class SimpleUserInfo {
    private String name;
    private String storename;
    private String roleName;

    public SimpleUserInfo(String name, String storename, String roleName) {
        this.name = name;
        this.storename = storename;
        this.roleName = roleName;
    }
}
