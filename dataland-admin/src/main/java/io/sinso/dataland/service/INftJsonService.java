package io.sinso.dataland.service;

import io.sinso.dataland.model.NftJson;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * @author lee
 * @since 2022-08-01
 */
public interface INftJsonService extends IService<NftJson> {

    NftJson findOneByFileUrl(String imageUrl);
}
