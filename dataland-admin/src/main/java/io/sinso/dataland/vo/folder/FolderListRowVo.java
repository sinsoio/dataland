package io.sinso.dataland.vo.folder;

import lombok.Data;

import java.util.List;

/**
 * @author hengbol
 * @date 6/25/22 5:24 PM
 */
@Data
public class FolderListRowVo {

    /**
     * Current file id
     */
    private Integer id;

    private String label;

    /**
     * Parent id
     */
    private Integer topId;

    private List<FolderListRowVo> children;
}
