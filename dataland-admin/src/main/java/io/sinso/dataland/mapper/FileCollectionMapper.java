package io.sinso.dataland.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.sinso.dataland.model.FileCollection;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author lee
 * @since 2022-06-13
 */
public interface FileCollectionMapper extends BaseMapper<FileCollection> {

    List<FileCollection> getMintStatistical(@Param("min") LocalDateTime min, @Param("max") LocalDateTime max, @Param("address") String address);

    List<FileCollection> getCollectionStatistical(@Param("min") LocalDateTime min, @Param("max") LocalDateTime max, @Param("collectionNum") Integer collectionNum);

}
