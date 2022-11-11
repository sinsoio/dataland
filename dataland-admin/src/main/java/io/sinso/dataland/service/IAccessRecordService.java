package io.sinso.dataland.service;

import io.sinso.dataland.model.AccessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lee
 * @since 2022-08-03
 */
public interface IAccessRecordService extends IService<AccessRecord> {

    /**
     * The access times of an interface were added
     *
     * @param accountId
     * @param interfaceName
     */
    void addAccessNum(Integer accountId, String interfaceName);

    /**
     * Verify the number of visits per day
     *
     * @param accountId
     * @param interfaceName
     */
    void validationDayAccessNum(Integer accountId, String interfaceName);

    /**
     * Verifies the number of accesses per second
     *
     * @param accountId
     * @param interfaceName
     * @return
     */
    Boolean validationSecondsAccessNum(Integer accountId, String interfaceName);

}
