<#assign swagger = config.swagger>
<#assign propsName = table.propsName>
package ${table.form.pkg};

import java.io.Serializable;
<#if config.lombok>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>
<#list table.form.imports as imp>
import ${imp};
</#list>
<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>

/**
 * ${table.comment!table.form.name}
 *
 * @author ${config.author}
 * ${date?string("yyyy-MM-dd HH:mm")}
 */
<#if config.lombok>
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
</#if>
<#if swagger>
@ApiModel(value="${propsName} object", description="${table.comment!}")
</#if>
public class ${table.form.name} implements Serializable {

    private static final long serialVersionUID = 1L;
<#--    <#if data.columnConst>-->
<#--    <#list table.columns as column>-->
<#--        <#if !column.inCommon>-->
<#--            public static final String ${column.name?upper_case} = "${column.name}";-->
<#--        </#if>-->
<#--    </#list>-->
<#--    </#if>-->
    <#list table.columns as column>
    <#if !column.inCommon && !column.tableLogic>
          <#if swagger>
    @ApiModelProperty(value = "${column.comment!column.name}")
        <#else>
    /**
     * ${column.comment!column.name}
     */
          </#if>
    private ${column.simpleJavaType} ${column.fieldName};
    </#if>
    </#list>

    <#--渲染get/set-->
    <#if !config.lombok>
    <#list table.columns as column>
    <#if !column.inCommon && !column.tableLogic>
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
