package ${table.dao.pkg};

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${table.entity.className};

/**
 * <p>
 * ${table.entity.name} 的 Dao 接口
 * </p>
 *
 * @author https://gitee.com/cnscoo/batiso
 * ${date?string("yyyy-MM-dd HH:mm")}
 */
@Mapper
public interface ${table.dao.name} extends BaseMapper<${table.entity.name}> {

}