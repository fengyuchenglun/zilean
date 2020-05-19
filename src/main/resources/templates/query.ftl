<#assign swagger = config.swagger>
<#assign propsName = table.propsName>
package ${table.query.pkg};

import java.io.Serializable;
<#if config.lombok>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>
<#list table.query.imports as imp>
import ${imp};
</#list>
<#if config.kim>
import com.kim.boot.web.converter.result.domain.PageSortQuery;
</#if>
<#if swagger>
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
</#if>

/**
 * ${table.comment!table.query.name}
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
public class ${table.query.name} <#if config.kim>extends PageSortQuery </#if>implements Serializable {

    private static final long serialVersionUID = 1L;

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
<#if !config.kim>
    /**
     * 页码
     */
    private Long pageNum = 1L;
    /**
     * 页大小
     */
    private Long pageSize = 1L;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序顺序
     */
    private String sortOrder;
</#if>

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
        <#if !config.kim>
    public Long getPageNum() {
        return this.pageNum;
    }

    public Long getPageSize() {
        return this.pageSize;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum=pageNum;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize=pageSize;
    }

    public String getSortField() {
        return this.sortField;
    }
    public String getSortOrder() {
        return this.sortQuery.getSortOrder();
    }

    public void setSortField(String sortField) {
        this.sortField=sortField;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder=sortOrder;
    }
        </#if>
    </#if>
}
