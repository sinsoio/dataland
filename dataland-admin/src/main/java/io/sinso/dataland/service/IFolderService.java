package io.sinso.dataland.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.sinso.dataland.model.Folder;
import io.sinso.dataland.vo.folder.*;

import java.util.List;
import java.util.Map;

/**
 * @author lee
 * @since 2022-06-13
 */
public interface IFolderService extends IService<Folder> {

    void addFolder(AddFolderVo addFolderVo);

    void updateFolder(UpdateFolderVo updateFolderVo);

    void delFolder(Integer id);


    /**
     * Get statistics on the number of files and folders
     *
     * @param parentId
     * @param searchMsg
     * @param type      1all 2created 3favorited  4collected
     * @return
     */
    FileStatisticalVo getFileStatistical(Integer parentId, String searchMsg, Integer type,
                                         Integer nftFormat);

    /**
     * @param parentId
     * @param uid
     * @return
     */
    Folder getByParentIdAndUserId(Integer parentId, Integer uid);

    /**
     * Gets the parent id of the previous level
     *
     * @param id
     * @return
     */
    Integer getTopId(Integer id);

    /**
     * getFolderList
     *
     * @param id
     * @return
     */
    List<FolderListRowVo> getFolderList(Integer id);

    /**
     * moveFolder
     *
     * @param moveFolderVo
     */
    void moveFolder(MoveFolderVo moveFolderVo);

    /**
     * getFilePath
     *
     * @param folderId
     * @return
     */
    List<BreadCrumbsJsonVo> getFilePath(Integer folderId);

    /**
     * @param pageNum
     * @param pageSize
     * @param parentId
     * @param type
     * @param searchMsg
     * @param state
     * @param nftFormat 1all 2image 3video 4audio 5 3DModel 6other
     * @return
     */
    Map getFileList(Integer pageNum, Integer pageSize, Integer parentId, Integer type,
                    String searchMsg, Integer state, Integer nftFormat);

    /**
     * getOneByParentId
     *
     * @param parentId
     * @return
     */
    Folder getOneByParentId(Integer parentId);
}
