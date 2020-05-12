package com.kim.zilean.model;

import com.intellij.database.model.DataType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.JDBCType;

/**
 * The type Table field.
 *
 * @author duanledexianxianxian
 * @date 2020 /5/10 10:09
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class TableField implements Serializable {
    private static final long serialVersionUID = -522912328270540965L;
    /**
     *  列名称（如 login_password）
     */
    private String name;

    private String type;
    /**
     * 属性名称（如 loginPassword）
     */
    private String fieldName;
    /**
     * 列对应的 Java 类型（非 java.lang 包下的类型含有包名）
     */
    private String javaType;
    /**
     * 列对应 Java 类型名称（不含包名）
     */
    private String simpleJavaType;
    /**
     * 数据库数据类型（如 varchar）
     */
    private DataType dataType;
    /**
     * JDBC类型
     */
    private JDBCType jdbcType;
    /**
     * 列的注释
     */
    private String comment;
    /**
     * 是否不为空（模板属性 notNull）
     */
    private boolean isNotNull = false;
    /**
     * 是否主键（模板属性 primaryKey）
     */
    private boolean isPrimaryKey = false;
    /**
     * 是否自增长（模板属性 autoGenerate）
     */
    private boolean isAutoGenerate = false;
    /**
     * 是否公共列（模板属性 inCommon）
     */
    private boolean isInCommon = false;
    /**
     *  是否逻辑列（模板属性 tableLogic）
     */
    private boolean isTableLogic = false;
    /**
     * 是否MySQL关键字（模板属性 mysqlKeyword）
     */
    private boolean isMysqlKeyword = false;
}
