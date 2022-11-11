package io.sinso.dataland.controller;

import io.sinso.dataland.service.IFolderService;
import io.sinso.dataland.util.UserLoginToken;
import io.sinso.dataland.vo.IdVo;
import io.sinso.dataland.vo.ResultVo;
import io.sinso.dataland.vo.folder.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author hengbol
 * @date 6/13/22 3:23 PM
 */
@Validated
@RestController
@RequestMapping("/api/data/folder")
@UserLoginToken
@Slf4j
public class FolderController {
    @Autowired
    private IFolderService folderService;

    /**
     * add folder
     *
     * @param addFolderVo
     * @return
     */
    @PostMapping("/add")
    public ResultVo addFolder(@Valid @RequestBody AddFolderVo addFolderVo) {
        folderService.addFolder(addFolderVo);
        return ResultVo.success();
    }

    /**
     * update folder name
     *
     * @param updateFolderVo
     * @return
     */
    @PostMapping("/update")
    public ResultVo updateFolder(@Valid @RequestBody UpdateFolderVo updateFolderVo) {
        folderService.updateFolder(updateFolderVo);
        return ResultVo.success();
    }

    /**
     * del folder
     *
     * @param idVo
     * @return
     */
    @PostMapping("/del")
    public ResultVo addFolder(@Valid @RequestBody IdVo idVo) {
        folderService.delFolder(idVo.getId());
        return ResultVo.success();
    }


    /**
     * Get statistics on the number of files and folders
     *
     * @param parentId
     * @param searchMsg
     * @param type      1all 2created 3favorited  4collected
     * @param nftFormat 1all 2image 3video 4audio 5 3DModel 6other
     * @return
     */
    @GetMapping("/get_file_statistical")
    public ResultVo getFileStatistical(Integer parentId,
                                       String searchMsg,
                                       Integer type,
                                       Integer nftFormat) {
        FileStatisticalVo fileStatisticalVo = folderService.getFileStatistical(parentId, searchMsg, type, nftFormat);
        return ResultVo.success(fileStatisticalVo);
    }

    /**
     * Gets the parent id of the previous level
     *
     * @param parentId The parent id of the current layer
     * @return
     */
    @GetMapping("/get_top_id")
    public ResultVo getTopId(@Valid @NotNull Integer parentId) {
        Integer topId = folderService.getTopId(parentId);
        return ResultVo.success(topId);
    }


    /**
     * Obtaining a file tree
     *
     * @param id
     * @return
     */
    @GetMapping("/get_folder_list")
    public ResultVo<List<FolderListRowVo>> getFolderList(@Valid @NotNull Integer id) {
        List<FolderListRowVo> listVoList = folderService.getFolderList(id);
        return ResultVo.success(listVoList);
    }

    /**
     * move Folder
     *
     * @param moveFolderVo
     * @return
     */
    @PostMapping("/move")
    public ResultVo moveFolder(@Valid @RequestBody MoveFolderVo moveFolderVo) {
        folderService.moveFolder(moveFolderVo);
        return ResultVo.success();
    }


    /**
     * Get folders and files
     *
     * @param pageNum
     * @param pageSize
     * @param parentId  Parent id of the next layer id of the folder at the local layer
     * @param type      1all 2created 3favorited  4collected
     * @param searchMsg The search field and parentId are mutually exclusive
     * @param state     1 Sort by time and 2 sort by name
     * @param nftFormat 1all 2image 3video 4audio 5 3DModel 6other
     * @return
     */
    @GetMapping("/get_file_list")
    public ResultVo<Map> getFileList(@Valid @NotNull @RequestParam @Min(1) Integer pageNum,
                                     @Valid @NotNull @Min(1) @Max(100) @RequestParam Integer pageSize,
                                     Integer parentId,
                                     @Valid @NotNull Integer type,
                                     String searchMsg,
                                     @Valid @NotNull Integer state,
                                     @Valid @NotNull Integer nftFormat) {
        Map fileList = folderService.getFileList(pageNum, pageSize, parentId, type, searchMsg, state, nftFormat);
        return ResultVo.success(fileList);
    }
}
