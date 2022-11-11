package io.sinso.dataland.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.enums.ResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.mapper.FolderMapper;
import io.sinso.dataland.model.FileCollection;
import io.sinso.dataland.model.Folder;
import io.sinso.dataland.service.IFileCollectionService;
import io.sinso.dataland.service.IFolderService;
import io.sinso.dataland.util.UserUtil;
import io.sinso.dataland.vo.file.FolderListVo;
import io.sinso.dataland.vo.folder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author lee
 * @since 2022-06-13
 */
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements IFolderService {

    @Autowired
    private IFileCollectionService fileCollectionService;

    @Autowired
    private FolderMapper folderMapper;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void addFolder(AddFolderVo addFolderVo) {
        Integer uid = UserUtil.getUid();
        Integer parentId = addFolderVo.getParentId();
        Folder folderOne = getOneByParentId(0);
        if (parentId.equals(0)) {
            parentId = folderOne.getId();
        }
        String folderName = addFolderVo.getFolderName();
        LambdaQueryWrapper<Folder> queryWrapper = Wrappers.<Folder>lambdaQuery()
                .eq(Folder::getUserId, uid)
                .eq(Folder::getParentId, parentId)
                .eq(Folder::getFolderName, folderName);
        Folder one = getOne(queryWrapper);
        if (one != null) {
            throw new BusinessException(ResCodeEnum.THIS_NAME_IS_ALREADY_USED);
        }
        Folder folder = new Folder();
        folder.setCreatedAt(LocalDateTime.now());
        folder.setFolderName(folderName);
        folder.setUserId(uid);
        folder.setParentId(parentId);
        save(folder);

        Folder folder1 = getById(parentId);
        if (folder1 == null) {
            throw new BusinessException(ResCodeEnum.NO_DATA);
        }
        folder = getById(folder.getId());
        updateById(folder);

    }

    @Override
    public void updateFolder(UpdateFolderVo updateFolderVo) {
        Integer uid = UserUtil.getUid();
        String folderName = updateFolderVo.getFolderName();
        Integer id = updateFolderVo.getId();
        Folder folder = getById(id);
        LambdaQueryWrapper<Folder> queryWrapper = Wrappers.<Folder>lambdaQuery()
                .eq(Folder::getUserId, uid)
                .eq(Folder::getParentId, folder.getParentId())
                .eq(Folder::getFolderName, folderName);
        Folder one = getOne(queryWrapper);
        if (one != null && !one.getId().equals(updateFolderVo.getId())) {
            throw new BusinessException(ResCodeEnum.THIS_NAME_IS_ALREADY_USED);
        }
        folder.setFolderName(folderName);
        updateById(folder);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void delFolder(Integer delFolderId) {
        Integer uid = UserUtil.getUid();
        LambdaQueryWrapper<Folder> queryWrapper = Wrappers.<Folder>lambdaQuery()
                .eq(Folder::getUserId, uid)
                .eq(Folder::getParentId, 0);
        Integer parentId = getOne(queryWrapper).getId();
        if (delFolderId == 0) {
            return;
        }
        boolean here = true;
        List<Integer> collect = new ArrayList<>();
        do {
            if (collect.size() == 0) {
                collect.add(delFolderId);
                fileCollectionService.delFolderMoveFile(delFolderId, parentId);
                removeById(delFolderId);
            }
            LambdaQueryWrapper<Folder> queryWrapperList = Wrappers.<Folder>lambdaQuery()
                    .in(Folder::getParentId, collect)
                    .eq(Folder::getUserId, uid);
            List<Folder> folderList = list(queryWrapperList);
            collect = folderList.stream().map(Folder::getId).distinct().collect(Collectors.toList());
            folderList.forEach(folder -> {
                Integer id = folder.getId();
                fileCollectionService.delFolderMoveFile(id, parentId);
                removeById(id);
            });
            if (collect.size() == 0) {
                here = false;
            }
        } while (here);
    }

    @Override
    public Folder getOneByParentId(Integer parentId) {
        Integer uid = UserUtil.getUid();
        LambdaQueryWrapper<Folder> queryWrapperOne = Wrappers.<Folder>lambdaQuery()
                .eq(Folder::getParentId, parentId)
                .eq(Folder::getUserId, uid);
        return getOne(queryWrapperOne);
    }


    @Override
    public FileStatisticalVo getFileStatistical(Integer parentId, String searchMsg, Integer type,
                                                Integer nftFormat) {
        if (parentId != null && parentId.equals(0)) {
            Folder folderOne = getOneByParentId(0);
            parentId = folderOne.getId();
        }
        QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("IFNULL(sum(1), 0) AS folderNum")
                .eq(Folder.USER_ID, UserUtil.getUid())
                .like(searchMsg != null, Folder.FOLDER_NAME, searchMsg)
                .eq(parentId != null, Folder.PARENT_ID, parentId);
        Map<String, Object> map = getMap(queryWrapper);
        Integer folderNum = Integer.valueOf(map.get("folderNum").toString());
        Integer fileNum = fileCollectionService.getFileStatistical(parentId, searchMsg, type,nftFormat);
        FileStatisticalVo fileStatisticalVo = new FileStatisticalVo();
        fileStatisticalVo.setFolderNum(folderNum);
        fileStatisticalVo.setFileNum(fileNum);
        return fileStatisticalVo;
    }

    @Override
    public Folder getByParentIdAndUserId(Integer parentId, Integer uid) {
        LambdaQueryWrapper<Folder> queryWrapper = Wrappers.<Folder>lambdaQuery()
                .eq(Folder::getParentId, parentId)
                .eq(Folder::getUserId, uid);
        return getOne(queryWrapper);
    }

    @Override
    public Integer getTopId(Integer parentId) {
        Folder folder = getById(parentId);
        if (folder == null) {
            return 0;
        }
        return folder.getParentId();
    }

    @Override
    public List<FolderListRowVo> getFolderList(Integer id) {
//        if (id.equals(0)) {
//            Folder folderOne = getOneByParentId(0);
//            id = folderOne.getId();
//        }
        LambdaQueryWrapper<Folder> queryWrapper = Wrappers.<Folder>lambdaQuery()
                .eq(Folder::getParentId, id)
                .eq(Folder::getUserId, UserUtil.getUid())
                .orderByDesc(Folder::getId);
        List<Folder> list = list(queryWrapper);
        return list.stream().map(folder -> {
            FolderListRowVo folderListRowVo = new FolderListRowVo();
            folderListRowVo.setId(folder.getId());
            folderListRowVo.setLabel(folder.getFolderName());
            folderListRowVo.setTopId(folder.getParentId());

            LambdaQueryWrapper<Folder> queryWrapper1 = Wrappers.<Folder>lambdaQuery()
                    .eq(Folder::getParentId, folder.getId());
            Folder folder1 = getOne(queryWrapper1);
            if (folder1 != null) {
                FolderListRowVo folderListRowVo1 = new FolderListRowVo();
                folderListRowVo1.setId(folder1.getId());
                folderListRowVo1.setLabel(folder1.getFolderName());
                folderListRowVo1.setTopId(folder1.getParentId());
                folderListRowVo1.setChildren(new ArrayList<>());
                folderListRowVo.setChildren(Collections.singletonList(folderListRowVo1));
            } else {
                folderListRowVo.setChildren(new ArrayList<>());
            }
            return folderListRowVo;
        }).collect(Collectors.toList());
    }

    @Override
    public void moveFolder(MoveFolderVo moveFolderVo) {
        Integer id = moveFolderVo.getId();
        Integer folderId = moveFolderVo.getFolderId();
        if (folderId.equals(id)) {
            return;
        }
        if (folderId == 0) {
            return;
        }
        Folder folder = getById(id);
        folder.setParentId(folderId);
        updateById(folder);
    }


    @Override
    public List<BreadCrumbsJsonVo> getFilePath(Integer folderId) {
        List<BreadCrumbsJsonVo> list = new ArrayList<>();
        Boolean bol = true;
        do {
            Folder folder = getById(folderId);
            folderId = folder.getParentId();
            if (folderId == 0) {
                bol = false;
            } else {
                String folderName = folder.getFolderName();
                BreadCrumbsJsonVo breadCrumbsJsonVo = new BreadCrumbsJsonVo();
                breadCrumbsJsonVo.setId(folder.getId());
                breadCrumbsJsonVo.setName(folderName);
                list.add(breadCrumbsJsonVo);
            }
        } while (bol);
        Collections.reverse(list);
        return list;
    }

    @Override
    public Map getFileList(Integer pageNum, Integer pageSize, Integer parentId,
                           Integer type, String searchMsg, Integer state, Integer nftFormat) {
        Integer limit = (pageNum - 1) * pageSize;
        Map map = new HashMap();
        Integer userId = UserUtil.getUid();
        if (parentId != null && parentId.equals(0)) {
            Folder folderOne = getOneByParentId(0);
            parentId = folderOne.getId();
        }
        Folder folderTopId = getById(parentId);
        FileStatisticalVo fileStatistical = getFileStatistical(parentId, searchMsg, 1,nftFormat);
        Integer totalNum = fileStatistical.getFileNum() + fileStatistical.getFolderNum();
        List<FileCollection> fileList = folderMapper.getFileList(parentId, type, searchMsg, state, userId, limit, pageSize, nftFormat);
        List<FolderListVo> collect = fileList.stream().map(fileCollection -> {
            FolderListVo folderListVo = new FolderListVo();
            if (fileCollection.getChain() == null) {
                folderListVo.setType(1);
            } else {
                folderListVo.setType(2);
            }
            folderListVo.setId(fileCollection.getId());
            folderListVo.setName(fileCollection.getNftName());
            folderListVo.setImageUrl(fileCollection.getImageUrl());
            folderListVo.setSourceUrl(fileCollection.getSourceUrl());
            folderListVo.setSinsoUrl(fileCollection.getSinsoUrl());
            folderListVo.setCollected(fileCollection.getCollected());
            folderListVo.setCreated(fileCollection.getCreated());
            folderListVo.setFavorited(fileCollection.getFavorited());
            folderListVo.setManuallyClick(fileCollection.getManuallyClick());
            folderListVo.setNftFormat(fileCollection.getNftFormat());
            folderListVo.setLogo(fileCollection.getLogo());
            folderListVo.setChain(fileCollection.getChain());
            folderListVo.setNftContract(fileCollection.getNftContract());
            return folderListVo;
        }).collect(Collectors.toList());
        //Determine if there is a next page
        if (totalNum > pageNum * pageSize) {
            map.put("isNext", true);
        } else {
            map.put("isNext", false);
        }
        map.put("topId", folderTopId == null ? null : folderTopId.getParentId());
        map.put("breadCrumbs", parentId == null ? null : getFilePath(parentId));
        map.put("fileList", collect);
        return map;
    }

}
