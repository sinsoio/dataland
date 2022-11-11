package io.sinso.dataland.vo.file;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hengbol
 * @date 6/14/22 2:17 PM
 */
@Data
public class MoveFileVo {
    /**
     * file id
     */
    @NotNull
    private Integer id;
    /**
     * folderId
     */
    @NotNull
    private Integer folderId;
}
