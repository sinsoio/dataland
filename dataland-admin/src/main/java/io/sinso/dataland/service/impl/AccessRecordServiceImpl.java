package io.sinso.dataland.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.enums.ResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.mapper.AccessRecordMapper;
import io.sinso.dataland.model.AccessRecord;
import io.sinso.dataland.service.IAccessRecordService;
import io.sinso.dataland.util.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author lee
 * @since 2022-08-03
 */
@Service
public class AccessRecordServiceImpl extends ServiceImpl<AccessRecordMapper, AccessRecord> implements IAccessRecordService {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void addAccessNum(Integer accountId, String interfaceName) {
        LambdaQueryWrapper<AccessRecord> queryWrapper = Wrappers.<AccessRecord>lambdaQuery()
                .eq(AccessRecord::getDate, LocalDate.now())
                .eq(AccessRecord::getInterfaceName, interfaceName)
                .eq(AccessRecord::getAccountId, accountId);
        AccessRecord one = getOne(queryWrapper);
        if (one == null) {
            AccessRecord accessRecord = new AccessRecord();
            accessRecord.setCreatedAt(LocalDateTime.now());
            accessRecord.setAccessNum(1);
            accessRecord.setInterfaceName(interfaceName);
            accessRecord.setDate(LocalDate.now());
            accessRecord.setAccountId(accountId);
            save(accessRecord);
            return;
        }
        one.setAccessNum(one.getAccessNum() + 1);
        updateById(one);
    }

    @Override
    public void validationDayAccessNum(Integer accountId, String interfaceName) {
        String dayKey = "day_accountId=" + accountId;
        String dayAccessNum = redisTemplate.opsForValue().get(dayKey);
        if (dayAccessNum == null) {
            dayAccessNum = "0";
        }
        int dayNum = Integer.valueOf(dayAccessNum) + 1;
        if (dayNum > 1000) {
            throw new BusinessException(ResCodeEnum.DAY_ACCESS_LIMIT);
        }
        long minutes = Duration.between(LocalDateTime.now(), LocalDateUtils.todayMinAndMax(LocalDateTime.now()).getMax()).toMinutes();
        redisTemplate.opsForValue().set(dayKey, Integer.toString(dayNum), minutes, TimeUnit.MINUTES);
        this.addAccessNum(accountId, interfaceName);
//        CompletableFuture.runAsync(() -> this.addAccessNum(accountId, interfaceName));

    }

    @Override
    public Boolean validationSecondsAccessNum(Integer accountId, String interfaceName) {
        String secondsKey = interfaceName + "seconds_accountId=" + accountId;
        String secondsAccessNum = redisTemplate.opsForValue().get(secondsKey);
        if (StringUtils.isBlank(secondsAccessNum)) {
            secondsAccessNum = "0";
        }
        int secondsNum = Integer.valueOf(secondsAccessNum) + 1;
        if (secondsNum > 3) {
            throw new BusinessException(ResCodeEnum.SECONDS_ACCESS_LIMIT);
        }
        redisTemplate.opsForValue().set(secondsKey, Integer.toString(secondsNum), 1, TimeUnit.SECONDS);
        return true;
    }

}
