package com.crane.template.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author crane
 * @date 2025.08.01 下午4:11
 * @description
 **/
public class GenerateUtils {
    static String projectPath = System.getProperty("user.dir");

    public static void main(String[] args) {
        // 使用 FastAutoGenerator 快速配置代码生成器
        FastAutoGenerator.create("jdbc:mysql://192.168.92.129:3306/easy_live?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("crane") // 设置作者
                            .disableOpenDir() // 生成后不打开文件夹
                            .outputDir("src\\main\\java"); // 输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.crane.template") // 设置父包名
                            .controller("controller")
                            .entity("model.po") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .serviceImpl("service.impl") // 设置 Service 实现类包名
                            .xml("mappers") // 设置 Mapper XML 文件包名
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml,
                                    projectPath + "/src/main/resources/mapper" // XML文件输出到resource目录
                            ));
                })
                .strategyConfig(builder -> {
                    builder.addInclude() // 设置需要生成的表名
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .controllerBuilder()
                            .enableRestStyle() // 启用 REST 风格
                            .serviceBuilder();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                .execute(); // 执行生成
    }
}
