<#assign propsName = table.propsName>
<#assign simpleClassName = table.simpleClassName>
<#assign kotlin = config.kotlin>
<#assign extConfig = config.extConfig>
package ${table.dao.pkg};

import ${table.entity.className};
import ${table.query.className};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${propsName} Mapper interface.
 *
 * @author ${config.author}
 * ${date?string("yyyy-MM-dd HH:mm")}
 */
public interface ${table.dao.name} extends BaseMapper<${table.entity.name}> {
    /**
     * query ${propsName} page list
     *
     * @param page the page
     * @param query  the query
     * @return the list
     */
    List<${table.entity.name}> queryList(IPage page, @Param("query") ${table.query.name} query);
}
