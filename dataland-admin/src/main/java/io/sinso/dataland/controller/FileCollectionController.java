package io.sinso.dataland.controller;

import io.sinso.dataland.service.IFileCollectionService;
import io.sinso.dataland.util.UserLoginToken;
import io.sinso.dataland.vo.IdVo;
import io.sinso.dataland.vo.ResultVo;
import io.sinso.dataland.vo.file.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author hengbol
 * @date 6/13/22 5:27 PM
 */
@Validated
@RestController
@RequestMapping("/api/data/file")
@UserLoginToken
@Slf4j
public class FileCollectionController {

    @Autowired
    private IFileCollectionService fileCollectionService;

    /**
     * collection
     *
     * @param collectionVo
     * @return
     */
    @PostMapping("/collection")
    public ResultVo collection(@Valid @RequestBody CollectionVo collectionVo) {
        fileCollectionService.collection(collectionVo);
        return ResultVo.success();
    }


    /**
     * remove file
     *
     * @param idVo
     * @return
     */
    @PostMapping("/remove")
    public ResultVo remove(@Valid @RequestBody IdVo idVo) {
        fileCollectionService.removeFile(idVo.getId());
        return ResultVo.success();
    }


    /**
     * move file
     *
     * @param moveFileVo
     * @return
     */
    @PostMapping("/move")
    public ResultVo moveFile(@Valid @RequestBody MoveFileVo moveFileVo) {
        fileCollectionService.moveFile(moveFileVo);
        return ResultVo.success();
    }


    /**
     * manually Click
     *
     * @param idVo
     * @return
     */
    @PostMapping("/manually_click")
    public ResultVo manuallyClick(@Valid @RequestBody IdVo idVo) {
        fileCollectionService.manuallyClick(idVo);
        return ResultVo.success();
    }

    /**
     * Obtain collection foundry ownership statistics
     *
     * @return
     */
    @GetMapping("/get_nft_statistical")
    public ResultVo<NftStatisticalVo> getNftStatistical() {
        NftStatisticalVo nftStatisticalVo = fileCollectionService.getNftStatistical();
        return ResultVo.success(nftStatisticalVo);
    }

    /**
     * get detail
     *
     * @param id
     * @return
     */
    @GetMapping("/get_detail")
    public ResultVo<FileDetailVo> getDetail(@Valid @NotNull Integer id) {
        FileDetailVo fileDetailVo = fileCollectionService.getDetail(id);
        return ResultVo.success(fileDetailVo);
    }

    /**
     * file Upload
     *
     * @param file
     */
    @PostMapping("file_upload")
    @UserLoginToken(required = false)
    public ResultVo<UploadFileVo> fileUpload(@Valid @NotNull @RequestParam(name = "file") MultipartFile file) {
        UploadFileVo uploadFileVo = fileCollectionService.fileUpload(file);
        return ResultVo.success(uploadFileVo);
    }

    /**
     * json file Upload
     *
     * @param uploadJsonStrVo 文件
     */
    @PostMapping("json_upload")
    public ResultVo<String> jsonUpload(@Valid @RequestBody UploadJsonStrVo uploadJsonStrVo) {
        String url = fileCollectionService.jsonUpload(uploadJsonStrVo);
        return ResultVo.success(url);
    }

    /**
     * mint Nft
     *
     * @param mintNftVo
     * @return
     */
    @PostMapping("mint_nft")
    public ResultVo mintNft(@Valid @RequestBody MintNftVo mintNftVo) {
        fileCollectionService.mintNft(mintNftVo);
        return ResultVo.success();
    }

    /**
     * transfer nft
     *
     * @param transferNftVo
     * @return
     */
    @PostMapping("transfer_nft")
    public ResultVo transferNft(@Valid @RequestBody TransferNftVo transferNftVo) {
        fileCollectionService.transferNft(transferNftVo);
        return ResultVo.success();
    }


    /**
     * Statistical collection casting
     *
     * @param time
     * @return
     */
    @GetMapping("/get_statistical")
    @UserLoginToken(required = false)
    public Object getStatistical(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time) {
        return fileCollectionService.getStatistical(time);
    }


    /**
     * Query new casting
     *
     * @param startAt
     * @param endAt
     * @param address
     * @return
     */
    @GetMapping("/get_mint_statistical")
    @UserLoginToken(required = false)
    public Object getMintStatistical(@Valid
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startAt,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endAt,
                                     String address) {
        return fileCollectionService.getMintStatistical(startAt, endAt, address);
    }

    /**
     * People who count the number of collections each day
     *
     * @param time
     * @return
     */
    @GetMapping("/get_collection_statistical")
    @UserLoginToken(required = false)
    public Object getCollectionStatistical(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time,
                                           Integer collectionNum) {
        return fileCollectionService.getCollectionStatistical(time, collectionNum);
    }
}
