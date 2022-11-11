package io.sinso.dataland.vo.file;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hengbol
 * @date 11/3/22 9:48 AM
 */
@Data
public class TransferNftVo {

    @NotNull
    private Integer id;

    @NotBlank
    private String toAddress;
}

