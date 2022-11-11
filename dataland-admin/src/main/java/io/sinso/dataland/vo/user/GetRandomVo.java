package io.sinso.dataland.vo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hengbol
 * @date 6/13/22 3:54 PM
 */
@Data
public class GetRandomVo {
    @NotBlank
    private String address;
}
