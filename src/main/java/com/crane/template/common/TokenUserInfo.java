package com.crane.template.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static com.crane.template.constants.FileConstants.AVATAR_FOLDER;

/**
 * @author crane
 * @date 2025.08.01 下午3:42
 * @description
 **/
@Data
public class TokenUserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -599863072327152923L;

    private String userId;

    public static void main(String[] args) {
        System.out.println(AVATAR_FOLDER);
    }
}
