package io.sinso.dataland.vo.file;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hengbol
 * @date 6/13/22 5:32 PM
 */
@Data
public class CollectionVo {
    @NotNull
    @NotBlank
    private String sourceUrl;

    /**
     * folderId
     */
    @NotNull
    private Integer folderId;
}
