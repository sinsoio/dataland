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
 * <p>
 * </p>
 *
 * @author lee
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("co_account")
public class Account implements Serializable {

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
     * walletAddress
     */
    private String walletAddress;

    /**
     * email
     */
    private String email;

    /**
     * projectName
     */
    private String projectName;

    /**
     * purpose
     */
    private String purpose;

    private String apiKey;

    /**
     * 1 Whitelist is available. 2 Blacklist is unavailable
     */
    private Integer state;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String CREATED_AT = "created_at";

    public static final String WALLET_ADDRESS = "wallet_address";

    public static final String EMAIL = "email";

    public static final String PROJECT_NAME = "project_name";

    public static final String PURPOSE = "purpose";

    public static final String API_KEY = "api_key";

    public static final String STATE = "state";

}
