package io.sinso.dataland.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.sinso.dataland.enums.ChainEnum;
import io.sinso.dataland.model.FileCollection;
import io.sinso.dataland.vo.IdVo;
import io.sinso.dataland.vo.PageResultVo;
import io.sinso.dataland.vo.account.ContentTypeEnum;
import io.sinso.dataland.vo.account.NftDetailApiPageVo;
import io.sinso.dataland.vo.file.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author lee
 * @since 2022-06-13
 */
public interface IFileCollectionService extends IService<FileCollection> {

    /**
     * collection
     *
     * @param collectionVo
     */
    void collection(CollectionVo collectionVo);

    /**
     * removeFile
     *
     * @param id
     */
    void removeFile(Integer id);

    /**
     * moveFile
     *
     * @param moveFileVo
     */
    void moveFile(MoveFileVo moveFileVo);


    /**
     * Obtain the unique by contract and nftid
     *
     * @param contract
     * @param nftId
     * @param uid
     * @param chain
     * @return
     */
    FileCollection getOneByContractAndNftId(String contract, String nftId, Integer uid, String chain);

    /**
     * manuallyClick
     *
     * @param idVo
     */
    void manuallyClick(IdVo idVo);

    /**
     * User nft statistics
     *
     * @return
     */
    NftStatisticalVo getNftStatistical();

    /**
     * File statistics
     *
     * @param parentId
     * @param searchMsg
     * @param type
     * @param nftFormat
     * @return
     */
    Integer getFileStatistical(Integer parentId, String searchMsg, Integer type,
                               Integer nftFormat);

    /**
     * Get the data to upload
     *
     * @return
     */
    FileCollection findOneUpload();

    /**
     * After deleting folders, batch move files to a new directory
     *
     * @param delFolderId
     * @param moveFolderId
     */
    void delFolderMoveFile(Integer delFolderId, Integer moveFolderId);

    /**
     * Obtain the data that failed to upload
     *
     * @return
     */
    FileCollection findOneUploadFail();


    /**
     * @param id
     * @return
     */
    FileDetailVo getDetail(Integer id);

    /**
     * removeFileByTxTokenId
     *
     * @param txTokenId
     */
    void removeFileByTxTokenId(String txTokenId);

    /**
     * fileUpload
     *
     * @param file
     * @return
     */
    UploadFileVo fileUpload(MultipartFile file);

    /**
     * mintNft
     *
     * @param mintNftVo
     */
    void mintNft(MintNftVo mintNftVo);

    /**
     * json Upload
     *
     * @param uploadJsonStrVo
     * @return
     */
    String jsonUpload(UploadJsonStrVo uploadJsonStrVo);


    /**
     * api interface acquisition
     *
     * @param pageNum
     * @param pageSize
     * @param contentType
     * @param chain
     * @param address
     * @return
     */
    PageResultVo<NftDetailApiPageVo> getNftFavoriteList(Integer pageNum, Integer pageSize, ContentTypeEnum contentType, ChainEnum chain, String address);

    /**
     * Statistical collection casting
     *
     * @param time
     * @return
     */
    Map getStatistical(LocalDate time);

    /**
     * Query new casting
     *
     * @param startAt
     * @param endAt
     * @param address
     * @return
     */
    List<DownloadStatisticalVo> getMintStatistical(LocalDateTime startAt, LocalDateTime endAt, String address);

    /**
     * Count people who have more collections than collectionNum
     *
     * @param time
     * @param collectionNum
     * @return
     */
    Object getCollectionStatistical(LocalDate time, Integer collectionNum);

    /**
     * transferNft
     *
     * @param transferNftVo
     */
    void transferNft(TransferNftVo transferNftVo);

    /**
     * get
     *
     * @param chainGetState
     * @return
     */
    FileCollection getOneByChainGetData(Integer chainGetState);

    /**
     * Update the data taken down the chain
     *
     * @param fileCollection
     */
    void updateChainGetData(FileCollection fileCollection);

    /**
     * Synchronize the same image to upload successfully
     *
     * @param imageUrl
     * @return
     */
    Boolean synSucUploadSinso(String imageUrl);

    /**
     * Synchronize the same image to upload successfully
     *
     * @param imageUrl
     * @param uploadSinsoFailMsg
     * @return
     */
    Boolean synFailUploadSinso(String imageUrl, String uploadSinsoFailMsg);
}

