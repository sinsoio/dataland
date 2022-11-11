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
 * @since 2022-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("co_scan_config")
public class ScanConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Version
    private Integer revision;

    private String apiKey;

    /**
     * Lock time - The number of calls used up the time available for the next call
     */
    private LocalDateTime keyLockAt;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String API_KEY = "api_key";

    public static final String KEY_LOCK_AT = "key_lock_at";

}
