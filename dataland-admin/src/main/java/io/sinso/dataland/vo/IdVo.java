package io.sinso.dataland.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author lee
 * @date 2021-02-20
 */
@Data
public class IdVo {
    /**
     * id
     */
    @NotNull
    @Positive
    private Integer id;
}
