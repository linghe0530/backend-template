package com.crane.template.constants;

import java.io.File;

/**
 * @author crane
 * @date 2025.08.02 下午8:28
 * @description
 **/
public interface FileConstants {


    String BASE_FOLDER = System.getProperty("user.dir");

    String FILE = "files";

    String AVATAR = "avatar";
    String AVATAR_FOLDER = BASE_FOLDER + File.separator + FILE + File.separator + AVATAR;
}
