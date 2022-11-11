package io.sinso.dataland.vo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * UserLoginVo
 *
 * @author : alibeibei
 * @date : 2020/07/17 15:47
 */
@Data
public class UserLoginVo {
    /**
     * address
     */
    @NotBlank
    private String address;

    /**
     * signature
     */
    @NotBlank
    private String signature;

    /**
     * Random validation field
     */
    @NotBlank
    private String message;


}
