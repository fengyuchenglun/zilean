package com.kim.zilean.generator.strategy;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

;import static com.kim.boot.code.generator.strategy.ModuleNameType.TYPE_ONE;

/**
 * 配置类
 *
 * @author duanledexianxianxian
 * @version 1.0.0
 * @date 2020 /1/6 23:49
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class Config {

    /**
     * 生成文件的输出目录 System.getProperty("user.dir")
     */
    private String outputDir = System.getProperty("user.dir");

    /**
     * xml是否输出到resources目录下
     */
    private Boolean xmlToResources = true;

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
    /**
     * 指定生成的主键的ID类型
     */
    private IdType idType;

    /**
     * 驱动连接的URL
     */
    private String url;
    /**
     * 驱动名称
     * 默认mysql
     */
    private String driverName = "com.mysql.cj.jdbc.Driver";
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;
    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "com.kim";
    /**
     * 模块名称
     */
    private String moduleName = null;
    ///**
    // * 系统模块名称
    // */
    //private String systemName;
    /**
     * 模块名称拼接模式
     */
    private ModuleNameType moduleNameType=TYPE_ONE;
    /**
     * 逻辑删除属性名称
     */
    private String logicDeleteFieldName = "is_deleted";

    /**
     * 表前缀，用逗号分隔
     */
    private String tablePrefix;
    /**
     * The Table prefixes.
     */
    @Setter(AccessLevel.NONE)
    private String[] tablePrefixes;

    /**
     * 表，用逗号分隔
     */
    private String table;

    /**
     * The Tables.
     */
    @Setter(AccessLevel.NONE)
    private String[] tables;

    /**
     * The Is user dir.
     */
    @Setter(AccessLevel.NONE)
    private Boolean isUserDir = true;

    /**
     * controller/api url 前缀
     */
    private String controllerUrlPrefix = "/api/v1/";

    /**
     * Sets table prefix.
     *
     * @param tablePrefix the table prefix
     * @return the table prefix
     */
    public Config setTablePrefix(String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            String[] prefixes = tablePrefix.split(",");
            for (String prefix : prefixes) {
                prefix += "_";
            }
            tablePrefixes = prefixes;
        }
        return this;
    }

    /**
     * Sets table.
     *
     * @param table the table
     * @return the table
     */
    public Config setTable(String table) {
        if (StringUtils.isNotBlank(table)) {
            tables = table.split(",");
        }
        return this;
    }

}
