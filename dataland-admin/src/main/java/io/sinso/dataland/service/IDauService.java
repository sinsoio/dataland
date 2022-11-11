package io.sinso.dataland.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.sinso.dataland.model.Dau;

import java.time.LocalDate;

/**
 * @author lee
 * @since 2022-10-09
 */
public interface IDauService extends IService<Dau> {
    /**
     * get Dau count
     *
     * @param startAt
     * @param endAt
     * @return
     */
    Integer getDau(LocalDate startAt, LocalDate endAt);

    /**
     * add Dau
     *
     * @param id
     * @param address
     */
    void addDau(Integer id, String address);
}
