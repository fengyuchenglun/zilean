package com.kim.zilean.model;

import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 界面配置类
 * 对应页面配置项
 *
 * @author duanledexianxianxian
 */
@Data
@Accessors(chain = true)
public class Config implements Serializable {
    private static final long serialVersionUID = 7933792178946657838L;
    /**
     * 生成文件的输出根目录 System.getProperty("user.dir")
     */
    private String basePath = System.getProperty("user.dir");

    /**
     * 父包名
     */
    private String parent = "com.kim.zilean";

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
     * （可空，标记数据是否删除的列，英文逗号分隔，不留空格）
     */
    private String logicColumn;
    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;
    /**
     * 是否打开文件
     */
    private boolean isOpen = false;
    /**
     * 是否使用Lombok
     */
    private boolean lombok = true;
    /**
     * 是否使用swagger
     */
    private boolean swagger = false;
    /**
     * 是否使用kotlin
     */
    private boolean kotlin = false;
    /**
     * 各个类型的配置
     */
    private PackageConfigs packageConfigs;
    /**
     * 需要生成的表信息列表
     */
    private List<TableInfo> tables;

    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.TIME_PACK;
    /**
     * 公共列
     * （可空，一般将公共列写入实体类的父类中，英文逗号分隔，不留空格）
     */
    private String commonColumn;
    /**
     * 是否为kim框架
     * 默认不使用
     */
    private boolean kim = false;

    /**
     * 自定义参数配置
     */
    private Map<String, Object> extConfig = Maps.newHashMap();

    /**
     * 获取公共列列表
     *
     * @return common columns
     */
    @JsonIgnore
    public List<String> getCommonColumns() {
        return commonColumn == null ? Collections.emptyList() : Arrays.asList(commonColumn.split(","));
    }

    /**
     * 判断列是否公共列
     *
     * @param column 列名称
     * @return 是否为公共列
     */
    public boolean isCommonColumn(String column) {
        return this.getCommonColumns().contains(column);
    }

    /**
     * 获取逻辑列列表
     *
     * @return
     */
    @JsonIgnore
    public List<String> getLogicColumns() {
        return logicColumn == null ? Collections.emptyList() : Arrays.asList(logicColumn.split(","));
    }

    /**
     * 判断列是否逻辑列
     *
     * @param column
     * @return
     */
    public boolean isLogicColumn(String column) {
        return this.getLogicColumns().contains(column);
    }
}
