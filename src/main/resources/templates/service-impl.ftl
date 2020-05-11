package ${table.serviceImpl.pkg};

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${table.entity.className};
import ${table.dao.className};
import ${table.service.className};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.entity.name} 服务实现类
 * </p>
 *
 * @author https://gitee.com/cnscoo/batiso
 * ${date?string("yyyy-MM-dd HH:mm")}
 */
@Service
public class ${table.serviceImpl.name} extends ServiceImpl<${table.dao.name}, ${table.entity.name}> implements ${table.service.name} {

}
