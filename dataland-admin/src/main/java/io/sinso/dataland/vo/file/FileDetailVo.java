package io.sinso.dataland.vo.file;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hengbol
 * @date 6/17/22 4:32 PM
 */
@Data
public class FileDetailVo {

    /**
     * nftName
     */
    private String nftName;

    /**
     * nftStanderd
     */
    private String nftStanderd;

    /**
     * nftContract
     */
    private String nftContract;

    /**
     * nftCreater
     */
    private String nftCreater;

    /**
     * nftId
     */
    private String nftId;

    /**
     * nft_holder
     */
    private String nftHolder;


    /**
     * mintingDate
     */
    private LocalDateTime mintingDate;

    /**
     * nft_format
     */
    private String nftFormat;

    /**
     * favorite_at
     */
    private LocalDateTime favoriteAt;

    /**
     * sourceUrl
     */
    private String sourceUrl;

    /**
     * If sinsour is nul show imageurl
     */
    private String imageUrl;

    /**
     * sinso url
     */
    private String sinsoUrl;

    /**
     * Whether it is my own collection (equivalent to purchase)
     */
    private Boolean collected;

    /**
     * manuallyClick
     */
    private Boolean manuallyClick;

    /**
     * created
     */
    private Boolean created;

    /**
     * favorited
     */
    private Boolean favorited;

    private String logo;

    private String chain;

    /**
     * attributes
     */
    private String attributes;


}
