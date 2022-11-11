package io.sinso.dataland.vo.file;

import io.sinso.dataland.enums.ChainEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hengbol
 * @date 7/23/22 2:13 PM
 */
@Data
public class MintNftVo {

    /**
     * folderId
     */
    @NotNull
    private Integer folderId;
    /**
     * nftName
     */
    @NotBlank
    private String nftName;

    /**
     * standerd
     */
    @NotBlank
    private String nftStanderd;

    /**
     * 合约nftContract地址
     */
    @NotBlank
    private String nftContract;

    /**
     * nft_creater
     */
    @NotBlank
    private String nftCreater;

    /**
     * nft_id
     */
    @NotBlank
    private String nftId;

    /**
     * minting_data
     */
    private Long mintingDate;

    /**
     * nft_format
     */
    @NotBlank
    private String nftFormat;
    /**
     * imageUrl
     */
    @NotBlank
    private String imageUrl;
    @NotBlank
    /**
     * logo url
     */
    private String logo;
    /**
     * chain
     */
    private ChainEnum chain;
    /**
     * Cast the uploaded jsonurl
     */
    @NotBlank
    private String jsonFileUrl;
    /**
     * mintingHash
     */
    @NotBlank
    private String mintingHash;
}
