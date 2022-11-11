package io.sinso.dataland.vo.file;

import io.sinso.dataland.model.FileCollection;
import lombok.Data;

import java.util.List;

/**
 * @author hengbol
 * @date 7/25/22 10:04 AM
 */
@Data
public class AccountAllNftVo {
    private List<FileCollection> list;
    /**
     * Whether to have the next page if not null
     */
    private String next;

}
