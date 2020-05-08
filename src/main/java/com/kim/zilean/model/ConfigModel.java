package com.kim.zilean.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 界面配置类
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
    /**
     * 表前缀，用逗号分隔
     */
    private String tablePrefix;
    /**
     * 表，用逗号分隔
     */
    private String tables;
    /**
     * controller/api url 前缀
     */
    private String controllerUrlPrefix = "/api/v1/";
    /**
     * 开发人员
     */
    private String author = "duanledexianxianxian";
    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;
    /**
     * 是否打开输出目录
     */
    private boolean open = false;
}