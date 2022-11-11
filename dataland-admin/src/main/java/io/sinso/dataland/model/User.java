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
 * @since 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("co_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Version
    private Integer revision;

    /**
     * registeredAt
     */
    private LocalDateTime registeredAt;

    /**
     * address
     */
    private String address;

    /**
     * chain
     */
    private String chain;

    /**
     * message
     */
    private String message;

    /**
     * Valid time of random field
     */
    private LocalDateTime messageEndAt;

    /**
     * token
     */
    private String token;

    /**
     * token validity time
     */
    private LocalDateTime tokenEndAt;

    /**
     * Initialization status 1 Uninitialized 2 Initializing 3 Initializing completed
     */
    private Integer type;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String REGISTERED_AT = "registered_at";

    public static final String ADDRESS = "address";

    public static final String CHAIN = "chain";

    public static final String MESSAGE = "message";

    public static final String MESSAGE_END_AT = "message_end_at";

    public static final String TOKEN = "token";

    public static final String TOKEN_END_AT = "token_end_at";

    public static final String TYPE = "type";

}
