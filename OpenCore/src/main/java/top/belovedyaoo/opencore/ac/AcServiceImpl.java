package top.belovedyaoo.opencore.ac;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.toolkit.TypeUtil;

import java.util.Comparator;
import java.util.List;

/**
 * 权限控制服务
 *
 * @param <T>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AcServiceImpl<T extends BaseFiled> extends TypeUtil<T> implements IService<T> {

    @Override
    public BaseMapper<T> getMapper() {
        return Mappers.ofEntityClass(getOriginalClass());
    }

    @Override
    public List<T> list() {
        return IService.super.list().stream()
                .filter(entity -> AcServiceStrategy.INSTANCE.getHandler(entity.getClass(),true).test(entity))
                .sorted(Comparator.comparingInt(BaseFiled::orderNum))
                .toList();
    }

}
