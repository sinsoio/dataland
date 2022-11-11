package io.sinso.dataland.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.sinso.dataland.model.Record;

/**
 *
 * @author lee
 * @since 2022-06-27
 */
public interface IRecordService extends IService<Record> {

    /**
     * Get the last piece of data
     *
     * @param uid
     * @param chain
     * @param blockNumber
     * @return
     */
    Record findOne(Integer uid, String chain, String blockNumber);

    /**
     *
     * @param address
     * @param uid
     */
    void addUserRecordByUserAddress(String address, Integer uid);

    /**
     *
     * @param address
     * @param uid
     * @return
     */
    void updateUserNft(String address, Integer uid);
}
