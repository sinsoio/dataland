package io.sinso.dataland.vo.file;

import lombok.Data;

/**
 * @author hengbol
 * @date 6/14/22 3:07 PM
 */
@Data
public class FolderListVo {

    /**
     * 1 Folder 2 files
     */
    private Integer type;

    /**
     * File id (the parent id of the next layer of clicks) or folder id
     */
    private Integer id;

    /**
     * name
     */
    private String name;

    /**
     * imageUrl
     */
    private String imageUrl;

    /**
     * sinsoUrl
     */
    private String sinsoUrl;


    /**
     * sourceUrl
     */
    private String sourceUrl;


    /**
     * Whether it is my own collection (equivalent to purchase)
     */
    private Boolean collected;


    private Boolean created;


    private Boolean favorited;

    /**
     * manuallyClick
     */
    private Boolean manuallyClick;

    /**
     * nft_format
     */
    private String nftFormat;

    /**
     * logo
     */
    private String logo;
    /**
     * chain
     */
    private String chain;

    /**
     * nftContract
     */
    private String nftContract;

}
