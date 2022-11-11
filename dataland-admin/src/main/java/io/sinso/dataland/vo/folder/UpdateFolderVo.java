package io.sinso.dataland.vo.folder;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hengbol
 * @date 6/13/22 3:38 PM
 */
@Data
public class UpdateFolderVo {
    /**
     * folderName
     */
    @NotBlank
    private String folderName;
    /**
     * id
     */
    @NotNull
    private Integer id;


}

