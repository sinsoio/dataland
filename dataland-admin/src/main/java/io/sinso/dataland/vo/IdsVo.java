package io.sinso.dataland.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lee
 * @date 2021-02-20
 */
@Data
public class IdsVo {
    /**
     * id
     */
    @NotNull
    List<Integer> ids;
}
