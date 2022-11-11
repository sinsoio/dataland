package io.sinso.dataland.mapper;

import io.sinso.dataland.model.ScanConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author lee
 * @since 2022-06-23
 */
public interface ScanConfigMapper extends BaseMapper<ScanConfig> {

    String getRandKey(@Param("keyLockAt")LocalDateTime now);
}
