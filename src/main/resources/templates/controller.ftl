<#assign propsName = table.propsName>
<#assign simpleClassName = table.simpleClassName>
<#assign kotlin = config.kotlin>
<#assign extConfig = config.extConfig>
<#assign swagger = config.swagger>
<#assign kim = config.kim>
package ${table.controller.pkg};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.fengyuchenglun.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
<#if kim>
import com.kim.boot.web.converter.result.domain.Add;
import com.kim.boot.web.converter.result.domain.Edit;
import com.kim.boot.web.mvc.annotation.KimController;
</#if>

import ${table.query.className};
import ${table.form.className};
import ${table.dto.className};
import ${table.vo.className};
import ${table.service.className};
<#if kim>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>

/**
* ${propsName} controller.
*
<#if table.comment??>
* ${table.comment}
*
</#if>
* @author ${config.author}
* @since ${date?string("yyyy-MM-dd HH:mm")}
*/
@Slf4j
<#if config.kim>
@KimController
<#else>
@RestController
</#if>
<#if swagger>
@Api(description = "${table.comment!propsName}")
</#if>
@RequestMapping("${config.controllerUrlPrefix}/${propsName}s")
public class ${table.controller.name} {

    @Autowired
    private ${table.service.name} ${table.service.propsName};

<#if extConfig.keyTableField??>
    <#assign keyFieldName = extConfig.keyTableField.fieldName>
    <#assign keyFieldType = extConfig.keyTableField.simpleJavaType>

    /**
    * add ${propsName}.
    *
    * @param form ${propsName} form object
    * @return the ${keyFieldType?lower_case}
    */
<#if swagger>
    @ApiOperation("add ${propsName}")
</#if>
    @PostMapping
    public ${keyFieldType} add${simpleClassName}(@RequestBody <#if config.kim>@Validated({Add.class}) </#if>${table.form.name} form) {
        return ${table.service.propsName}.add${simpleClassName}(form);
    }

    /**
    * remove ${propsName}.
    *
    * @param ${keyFieldName} the ${keyFieldName}
    * @return the boolean
    */
<#if swagger>
    @ApiOperation("remove ${propsName}")
</#if>
    @DeleteMapping("/{${keyFieldName}}")
    public Boolean remove${simpleClassName}(@PathVariable("${keyFieldName}") ${keyFieldType} ${keyFieldName}) {
        return ${table.service.propsName}.remove${simpleClassName}(${keyFieldName});
    }
<#else>
    /**
    * add ${propsName}.
    *
    * @param form ${propsName} form object
    * @return the boolean
    */
<#if swagger>
    @ApiOperation("add ${propsName}")
</#if>
    @PostMapping
    public Boolean add${simpleClassName}(@RequestBody <#if config.kim>@Validated({Add.class}) </#if>${table.form.name} form) {
        return ${table.service.propsName}.add${simpleClassName}(form);
    }
</#if>

    /**
    * edit ${propsName}.
    *
    * @param form ${propsName} form object
    * @return the boolean
    */
<#if swagger>
    @ApiOperation("edit ${propsName}")
</#if>
    @PutMapping
    public Boolean edit${simpleClassName}(@RequestBody <#if config.kim>@Validated({Edit.class}) </#if>${table.form.name} form) {
        return ${table.service.propsName}.edit${simpleClassName}(form);
    }

    /**
    * get ${propsName} page List.
    *
    * @param query the ${propsName} query object
    * @return page
    */
<#if swagger>
    @ApiOperation("get ${propsName} page List")
</#if>
    @GetMapping
    public IPage<${table.vo.name}> get${simpleClassName}PageList(${table.query.name} query) {
        IPage<${table.dto.name}> page = ${table.service.propsName}.get${simpleClassName}PageList(query);
        if (null != page) {
            return page.setRecords(BeanUtils.copyList(page.getRecords(), ${table.vo.name}.class));
        }
        return null;
    }

}





