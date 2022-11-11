package io.sinso.dataland.vo.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hengbol
 * @date 8/2/22 3:09 PM
 */
@Data
public class AccountApplyVo {

    /**
     * walletAddress
     */
    @NotBlank
    private String walletAddress;

    /**
     * email
     */
    @NotBlank
    private String email;

    /**
     * projectName
     */
    @NotBlank
    private String projectName;

    /**
     * purpose
     */
    @NotBlank
    private String purpose;
}
