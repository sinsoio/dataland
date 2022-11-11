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
 * @since 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("co_record")
public class Record implements Serializable {

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
     * from
     */
    private String txFromAddress;

    /**
     * to
     */
    private String txToAddress;

    /**
     * txSendAddress
     */
    private String txSendAddress;

    /**
     * txReceiveAddress
     */
    private String txReceiveAddress;

    /**
     * nftscan id
     */
    private String txTokenId;

    /**
     * txTime
     */
    private LocalDateTime txTime;

    /**
     * blockNumber
     */
    private String blockNumber;

    /**
     * 区块hash
     */
    private String blockHash;

    /**
     * txHash
     */
    private String txHash;

    /**
     * chain
     */
    private String chain;

    /**
     * 1 received
     * 2 is sent out
     */
    private Integer type;

    /**
     * 1 The processing is not processed. 2 The processing is complete
     */
    private Boolean state;

    private Integer userId;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String CREATED_AT = "created_at";

    public static final String TX_FROM_ADDRESS = "tx_from_address";

    public static final String TX_TO_ADDRESS = "tx_to_address";

    public static final String TX_SEND_ADDRESS = "tx_send_address";

    public static final String TX_RECEIVE_ADDRESS = "tx_receive_address";

    public static final String TX_TOKEN_ID = "tx_token_id";

    public static final String TX_TIME = "tx_time";

    public static final String BLOCK_NUMBER = "block_number";

    public static final String BLOCK_HASH = "block_hash";

    public static final String TX_HASH = "tx_hash";

    public static final String CHAIN = "chain";

    public static final String TYPE = "type";

    public static final String STATE = "state";
    public static final String USER_ID = "user_id";

}
