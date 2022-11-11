package io.sinso.dataland.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.mapper.DauMapper;
import io.sinso.dataland.model.Dau;
import io.sinso.dataland.service.IDauService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author lee
 * @since 2022-10-09
 */
@Service
public class DauServiceImpl extends ServiceImpl<DauMapper, Dau> implements IDauService {

    @Override
    public Integer getDau(LocalDate startAt, LocalDate endAt) {
        LambdaQueryWrapper<Dau> queryWrapper = Wrappers.<Dau>lambdaQuery()
                .ge(Dau::getDate, startAt)
                .le(Dau::getDate, endAt);
        return count(queryWrapper);
    }

    @Override
    public void addDau(Integer userId, String address) {
        try {
            LambdaQueryWrapper<Dau> queryWrapper = Wrappers.<Dau>lambdaQuery()
                    .eq(Dau::getDate, LocalDate.now())
                    .eq(Dau::getUserId, userId)
                    .eq(Dau::getAddress, address);
            Dau one = getOne(queryWrapper);
            if (one != null) {
                return;
            }
            Dau dau = new Dau();
            dau.setCreatedAt(LocalDateTime.now());
            dau.setUserId(userId);
            dau.setAddress(address);
            dau.setDate(LocalDate.now());
            save(dau);
        } catch (Exception e) {
        }

    }
}
