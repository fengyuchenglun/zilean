<#assign propsName = table.propsName>
<#assign simpleClassName = table.simpleClassName>
<#assign kotlin = config.kotlin>
<#assign extConfig = config.extConfig>
package ${table.serviceImpl.pkg};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kim.boot.util.BeanUtils;
import ${table.query.className};
import ${table.form.className};
import ${table.entity.className};
import ${table.dto.className};
import ${table.dao.className};
import ${table.service.className};

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* ${propsName} service.
*
* @author ${config.author}
* @since ${date?string("yyyy-MM-dd HH:mm")}
*/
@Service
@Slf4j
public class ${table.serviceImpl.name} extends ServiceImpl<${table.dao.name}, ${table.entity.name}> implements ${table.service.name}{

 <#if extConfig.keyTableField??>
  <#assign keyFieldName = extConfig.keyTableField.fieldName>
  <#assign keyFieldType = extConfig.keyTableField.simpleJavaType>

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ${keyFieldType} add${simpleClassName}(${table.form.name} form) {
        // todo pre check
        ${table.entity.name} entity = BeanUtils.copyObject(form, ${table.entity.name}.class);
        this.save(entity);
        return entity.get${keyFieldName?cap_first}();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove${simpleClassName}(${keyFieldType} ${keyFieldName}) {
        return this.removeById(${keyFieldName});
    }

 <#else>
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean add${simpleClassName}(${table.form.name} form) {
        // todo pre check
        ${table.entity.name} entity = BeanUtils.copyObject(form, ${table.entity.name}.class);
        return this.save(entity);
    }
 </#if>

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean edit${simpleClassName}(${table.form.name} form) {
        // todo pre check
        ${table.entity.name} entity = BeanUtils.copyObject(form, ${table.entity.name}.class);
        return this.updateById(entity);
    }

    @Override
    public IPage<${table.dto.name}> get${simpleClassName}PageList(${table.query.name} query) {
        IPage page = new Page(query.getPageNum(), query.getPageSize());
        page.setRecords(this.baseMapper.queryList(page, query));
        return page;
    }
 }
