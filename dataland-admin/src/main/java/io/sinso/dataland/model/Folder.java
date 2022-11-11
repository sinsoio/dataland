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
@TableName("co_folder")
public class Folder implements Serializable {

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
     * folderName
     */
    private String folderName;

    /**
     * userId
     */
    private Integer userId;

    /**
     * Parent id 0 indicates that there is no parent
     */
    private Integer parentId;


    public static final String ID = "id";

    public static final String REVISION = "revision";

    public static final String CREATED_AT = "created_at";

    public static final String FOLDER_NAME = "folder_name";

    public static final String USER_ID = "user_id";

    public static final String PARENT_ID = "parent_id";

}
