package com.crane.template.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.Collections;
import java.util.List;

/**
 * @author crane
 * @date 2025.06.26 下午12:03
 * @description
 **/
public class CollUtils extends CollUtil {

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }
}
