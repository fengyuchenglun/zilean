package com.kim.zilean.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 界面配置类
 * 对应页面配置项
 *
 * @author duanledexianxianxian
 */
@Data
@Accessors(chain = true)
public class Config implements Serializable {

    /**
     * 生成文件的输出根目录 System.getProperty("user.dir")
     */
    private String outputDir = System.getProperty("user.dir");

    /**
     * 父包名
     */
    private String parent="com.kim.zilean";

    /**
     * 父包模块名
     */
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
     * controller/api url 前缀
     */
    private String controllerUrlPrefix = "/api/v1/";
    /**
     * 逻辑删除列
     */
    private String logicDeleteFieldName;
    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;
    /**
     *  各个类型的配置
     */
    private PackageConfigs packageConfigs;
    /**
     * 需要生成的表信息列表
     */
    private List<TableInfo> tables;

}
