package io.sinso.dataland.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lee
 * @since 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("co_nft_json")
public class NftJson implements Serializable {

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
     * json detail
     */
    private String jsonStr;

    /**
     * cid
     */
    private String cid;

    /**
     * fileUrl
     */
    private String fileUrl;

    /**
     * uploadAddress  1oci 2getway
     */
    private Integer uploadAddress;

    /**
     * nftContract
     */
    private String nftContract;

    /**
     * nft_id
     */
    private String nftId;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String CREATED_AT = "created_at";

    public static final String JSON_STR = "json_str";

    public static final String CID = "cid";

    public static final String FILE_URL = "file_url";

    public static final String UPLOAD_ADDRESS = "upload_address";

    public static final String NFT_CONTRACT = "nft_contract";

    public static final String NFT_ID = "nft_id";

}
