package io.sinso.dataland.controller.api;

import io.sinso.dataland.enums.ChainEnum;
import io.sinso.dataland.service.IAccountService;
import io.sinso.dataland.service.IFileCollectionService;
import io.sinso.dataland.util.UserLoginToken;
import io.sinso.dataland.vo.PageResultVo;
import io.sinso.dataland.vo.ResultVo;
import io.sinso.dataland.vo.account.AccountApplyVo;
import io.sinso.dataland.vo.account.ContentTypeEnum;
import io.sinso.dataland.vo.account.NftDetailApiPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * api Account management
 *
 * @author hengbol
 * @date 8/2/22 10:04 AM
 */
@Validated
@RestController
@RequestMapping("/api/account")
@Slf4j
@UserLoginToken
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IFileCollectionService fileCollectionService;

    /**
     * apply apikey
     *
     * @param accountApplyVo
     * @return
     */
    @PostMapping("/apply_api_key")
    @UserLoginToken(required = false)
    public ResultVo applyApiKey(@Valid @RequestBody AccountApplyVo accountApplyVo) {
        String apiKey = accountService.applyApiKey(accountApplyVo);
        return ResultVo.success(apiKey);
    }

    /**
     * refresh apikey
     *
     * @return
     */
    @PostMapping("/refresh_api_key")
    public ResultVo refreshApiKey() {
        String apiKey = accountService.refreshApiKey();
        return ResultVo.success(apiKey);
    }

    /**
     * get nft Detail
     *
     * @param pageNum
     * @param pageSize
     * @param contentType image  video  audio  all
     * @param chain
     * @param address
     * @return
     */
    @GetMapping("/get_nft_favorite_list")
    public ResultVo<PageResultVo<NftDetailApiPageVo>> getNftFavoriteList(@Valid @NotNull @RequestParam @Min(1) Integer pageNum,
                                                                         @Valid @NotNull @Min(1) @Max(100) @RequestParam Integer pageSize,
                                                                         @Valid @NotNull ContentTypeEnum contentType,
                                                                         @Valid @NotNull ChainEnum chain,
                                                                         @Valid @NotBlank String address) {
        PageResultVo<NftDetailApiPageVo> pageResultVo = fileCollectionService.getNftFavoriteList(pageNum, pageSize, contentType, chain, address);
        return ResultVo.success(pageResultVo);
    }
}
