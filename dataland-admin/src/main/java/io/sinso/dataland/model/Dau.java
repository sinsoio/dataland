package io.sinso.dataland.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author lee
 * @since 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("co_dau")
public class Dau implements Serializable {

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
     * userId
     */
    private Integer userId;

    /**
     * address
     */
    private String address;

    /**
     * date
     */
    private LocalDate date;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String CREATED_AT = "created_at";

    public static final String USER_ID = "user_id";

    public static final String ADDRESS = "address";

    public static final String DATE = "date";

}
