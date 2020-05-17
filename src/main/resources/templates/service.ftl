<#assign propsName = table.propsName>
<#assign simpleClassName = table.simpleClassName>
<#assign kotlin = config.kotlin>
<#assign extConfig = config.extConfig>
package ${table.service.pkg};
import ${table.query.className};
import ${table.form.className};
import ${table.entity.className};
import ${table.dto.className};
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* ${propsName} service interface.
*
* @author ${config.author}
* @since ${date?string("yyyy-MM-dd HH:mm")}
*/
public interface ${table.service.name} extends IService<${table.entity.name}> {

 <#if extConfig.keyTableField??>
  <#assign keyFieldName = extConfig.keyTableField.fieldName>
  <#assign keyFieldType = extConfig.keyTableField.simpleJavaType>
    /**
     * add ${propsName}
     *
     * @param form the ${propsName} form
     * @return the ${keyFieldType?lower_case}
     */
    ${keyFieldType} add${simpleClassName}(${table.form.name} form);

    /**
     * remove ${propsName}
     *
     * @param ${keyFieldName} the ${keyFieldName}
     * @return the boolean
     */
    Boolean remove${simpleClassName}(${keyFieldType} ${keyFieldName});
 <#else>
    /**
     * add ${propsName}
     *
     * @param form the ${propsName} form
     * @return the boolean
     */
    Boolean add${simpleClassName}(${table.form.name} form);
 </#if>

    /**
     * edit ${propsName}
     *
     * @param form the ${propsName} form
     * @return the boolean
     */
    Boolean edit${simpleClassName}(${table.form.name} form);

    /**
     * get ${propsName} page List.
     *
     * @param query the ${propsName} query object
     * @return page
     */
    IPage<${table.dto.name}> get${simpleClassName}PageList(${table.query.name} query);
 }

