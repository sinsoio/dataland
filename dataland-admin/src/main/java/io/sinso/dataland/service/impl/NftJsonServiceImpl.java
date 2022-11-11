package io.sinso.dataland.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.mapper.NftJsonMapper;
import io.sinso.dataland.model.NftJson;
import io.sinso.dataland.service.INftJsonService;
import org.springframework.stereotype.Service;

/**
 *
 * @author lee
 * @since 2022-08-01
 */
@Service
public class NftJsonServiceImpl extends ServiceImpl<NftJsonMapper, NftJson> implements INftJsonService {

    @Override
    public NftJson findOneByFileUrl(String imageUrl) {
        LambdaQueryWrapper<NftJson> queryWrapper = Wrappers.<NftJson>lambdaQuery()
                .eq(NftJson::getFileUrl, imageUrl);
        return getOne(queryWrapper);
    }
}
