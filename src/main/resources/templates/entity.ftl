package ${table.entity.pkg};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
<#if config.lombok>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>
<#if config.swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#list table.entity.imports as imp>
import ${imp};
</#list>


/**
 * ${table.comment!table.entity.name}
 *
 * @author ${config.author}
 * ${date?string("yyyy-MM-dd HH:mm")}
 */
<#if config.lombok>
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
</#if>
@TableName("${table.name}")
<#if config.swagger>
@ApiModel(description = "${table.comment!table.entity.name}")
</#if>
public class ${table.entity.name} implements Serializable {

    private static final long serialVersionUID = 1L;
<#--    <#if data.columnConst>-->
<#--    <#list table.columns as column>-->
<#--        <#if !column.inCommon>-->
<#--            public static final String ${column.name?upper_case} = "${column.name}";-->
<#--        </#if>-->
<#--    </#list>-->
<#--    </#if>-->
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
    <#if config.swagger>
    @ApiModelProperty(value = "${column.comment}")
    </#if>
    private ${column.simpleJavaType} ${column.fieldName};
    </#if>
    </#list>

    <#--渲染get/set-->
    <#if !config.lombok>
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
