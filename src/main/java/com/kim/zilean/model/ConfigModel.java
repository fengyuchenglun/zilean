package com.kim.zilean.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 界面配置类
 * 对应页面配置项
 *
 * @author duanledexianxianxian
 */
@Data
@Accessors(chain = true)
public class ConfigModel implements Serializable {

    /**
     * 生成文件的输出根目录 System.getProperty("user.dir")
     */
    private String outputDir = System.getProperty("user.dir");

    private String parent;

    private String moduleName;

    /**
     * 表前缀，用逗号分隔
     */
    private String tablePrefix;

    /**
     * 开发人员
     */
    private String author = "duanledexianxianxian";
    /**
     * 表，用逗号分隔
     */
    private String tables;
    /**
     * controller/api url 前缀
     */
    private String controllerUrlPrefix = "/api/v1/";

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;
    /**
     * 是否打开输出目录
     */
    private boolean open = false;
    /**
     * entity包路径
     */
    private String entityPackage;
    /**
     * dto包路径
     */
    private String dtoPackage;
    /**
     * vo包路径
     */
    private String voPackage;
    /**
     * query包路径
     */
    private String queryPackage;
    /**
     * form包路径
     */
    private String formPackage;
    /**
     * dao包路径
     */
    private String daoPackage;
    /**
     * xml包路径
     */
    private String xmlPath;
    /**
     * service包路径
     */
    private String servicePackage;
    /**
     * serviceImpl包路径
     */
    private String serviceImplPackage;
    /**
     * controller包路径
     */
    private String controllerPackage;

}
