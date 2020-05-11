package ${table.entity.pkg};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
<#if data.lombok>
import lombok.Getter;
import lombok.Setter;
</#if>
<#if data.swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#list table.entity.imports as imp>
import ${imp};
</#list>
<#if data.entitySuperClass??>
import ${data.entitySuperClass};
</#if>

/**
 * ${table.comment!table.entity.name}
 *
 * @author https://gitee.com/cnscoo/batiso
 * ${date?string("yyyy-MM-dd HH:mm")}
 */
<#if data.lombok>
@Getter
@Setter
</#if>
@TableName("${table.name}")
<#if data.swagger>
@ApiModel(description = "${table.comment!table.entity.name}")
</#if>
public class ${table.entity.name}<#if data.entitySuperClass??> extends ${data.simpleEntitySuperClass}</#if> {
    <#if data.columnConst>
    <#list table.columns as column><#if !column.inCommon>
    public static final String ${column.name?upper_case} = "${column.name}";
    </#if></#list>
    </#if>
    <#list table.columns as column>
    <#if !column.inCommon>

    /**
     * ${column.comment!column.name}
     */
    <#if column.mysqlKeyword>
    @TableField("`${column.name}`")
    </#if>
    <#if column.primaryKey>
    <#if column.autoGenerate>
    @TableId(type = IdType.AUTO)
    <#else>
    @TableId
    </#if>
    </#if>
    <#if column.tableLogic>
    @TableLogic
    </#if>
    <#if data.swagger>
    @ApiModelProperty(value = "${column.comment}")
    </#if>
    private ${column.simpleJavaType} ${column.fieldName};
    </#if>
    </#list>

    <#if !data.lombok>
    <#list table.columns as column>
    <#if !column.inCommon>
    public ${column.simpleJavaType} get${column.fieldName?cap_first}() {
        return this.${column.fieldName};
    }

    public void set${column.fieldName?cap_first}(${column.simpleJavaType} ${column.fieldName}) {
        this.${column.fieldName} = ${column.fieldName};
    }

    </#if>
    </#list>
    </#if>
}
