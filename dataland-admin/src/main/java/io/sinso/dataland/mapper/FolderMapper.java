package io.sinso.dataland.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.sinso.dataland.model.FileCollection;
import io.sinso.dataland.model.Folder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author lee
 * @since 2022-06-13
 */
public interface FolderMapper extends BaseMapper<Folder> {

    List<FileCollection> getFileList(@Param("parentId") Integer parentId, @Param("type") Integer type,
                                     @Param("searchMsg") String searchMsg, @Param("state") Integer state,
                                     @Param("userId") Integer userId, @Param("limit") Integer limit,
                                     @Param("pageSize") Integer pageSize, @Param("nftFormat") Integer nftFormat);
}
