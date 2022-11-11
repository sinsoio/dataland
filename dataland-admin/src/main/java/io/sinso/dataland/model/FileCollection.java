package io.sinso.dataland.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author lee
 * @since 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("co_file_collection")
public class FileCollection implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Version
    private Integer revision;

    /**
     * createdAt
     */
    private LocalDateTime createdAt;

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
     * nft_id
     */
    private String nftId;

    /**
     * nft_holder
     */
    private String nftHolder;


    /**
     * minting_hash createdAt
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
     * imageUrl
     */
    private String imageUrl;

    /**
     * sinsoUrl
     */
    private String sinsoUrl;

    /**
     * Is it your own collection (equivalent to buying owner)
     */
    @TableField("is_collected")
    private Boolean collected;

    /**
     * Whether to manually click Upload
     */
    @TableField("is_manually_click")
    private Boolean manuallyClick;

    /**
     * Whether you created it yourself
     */
    @TableField("is_created")
    private Boolean created;

    /**
     * Is it your own interest (equivalent to collection)
     */
    @TableField("is_favorited")
    private Boolean favorited;

    /**
     * Upload sisnogetay status 1 to be uploaded 2 Upload succeeded 3 Upload failed 4 Do not upload without the source image
     */
    private Integer uploadSinsoState;
    /**
     * uploadSinsoAt
     */
    private LocalDateTime uploadSinsoAt;
    /**
     * Upload failure Next upload time
     */
    private LocalDateTime uploadSinsoFailAt;
    /**
     * Upload failure Cause
     */
    private String uploadSinsoFailMsg;

    /**
     * 删除状态Delete the state
     */
    @TableField("is_del")
    private Boolean del;
    @TableField("is_mint")
    private Boolean mint;

    /**
     * delAt
     */
    private LocalDateTime delAt;

    private Integer userId;

    private Integer folderId;


    private String logo;
    private String chain;
    private String mintingHash;
    private String attributes;
    /**
     * On-chain data acquisition status 1 is not obtained, 2 is obtained successfully and 3 is obtained failed
     */
    private Integer chainGetState;
    private String tokenUri;
    private String chainGetStateFailMsg;
    private LocalDateTime chainGetStateFailAt;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String CREATED_AT = "created_at";

    public static final String NFT_NAME = "nft_name";

    public static final String NFT_STANDERD = "nft_standerd";

    public static final String NFT_CONTRACT = "nft_contract";

    public static final String NFT_CREATER = "nft_creater";

    public static final String NFT_ID = "nft_id";

    public static final String NFT_HOLDER = "nft_holder";

    public static final String MINTING_DATE = "minting_date";

    public static final String NFT_FORMAT = "nft_format";

    public static final String FAVORITE_AT = "favorite_at";

    public static final String SOURCE_URL = "source_url";

    public static final String IMAGE_URL = "image_url";

    public static final String SINSO_URL = "sinso_url";

    public static final String IS_COLLECTED = "is_collected";

    public static final String IS_CREATED = "is_created";

    public static final String IS_FAVORITED = "is_favorited";

    public static final String UPLOAD_SINSO_STATE = "upload_sinso_state";

    public static final String IS_DEL = "is_del";

    public static final String DEL_AT = "del_at";

    public static final String USER_ID = "user_id";

    public static final String FOLDER_ID = "folder_id";

    public static final String IS_MANUALLY_CLICK = "is_manually_click";

    public static final String UPLOAD_SINSO_AT = "upload_sinso_at";

    public static final String UPLOAD_SINSO_FAIL_AT = "upload_sinso_fail_at";

    public static final String UPLOAD_SINSO_FAIL_MSG = "upload_sinso_fail_msg";
    public static final String LOGO = "logo";
    public static final String CHAIN = "chain";
    public static final String MINTING_HASH = "minting_hash";

    public static final String ATTRIBUTES = "attributes";
    public static final String IS_MINT = "is_mint";
    public static final String CHAIN_GET_STATE = "chain_get_state";
    public static final String CHAIN_GET_STATE_FAIL_AT = "chain_get_state_fail_at";
    public static final String CHAIN_GET_STATE_FAIL_MSG = "chain_get_state_fail_msg";
    public static final String TOKEN_URI = "token_uri";
}
