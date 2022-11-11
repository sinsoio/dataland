package io.sinso.dataland.vo.folder;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hengbol
 * @date 6/14/22 2:17 PM
 */
@Data
public class MoveFolderVo {
    /**
     * folder id
     */
    @NotNull
    private Integer id;
    /**
     * id of the folder you moved to
     */
    @NotNull
    private Integer folderId;
}
