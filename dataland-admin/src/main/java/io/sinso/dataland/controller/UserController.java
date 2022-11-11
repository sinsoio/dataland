package io.sinso.dataland.controller;

import io.sinso.dataland.service.IDauService;
import io.sinso.dataland.service.IUserService;
import io.sinso.dataland.util.UserLoginToken;
import io.sinso.dataland.vo.ResultVo;
import io.sinso.dataland.vo.user.GetRandomVo;
import io.sinso.dataland.vo.user.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author hengbol
 * @date 6/13/22 2:42 PM
 */
@Validated
@RestController
@RequestMapping("/api/data/user")
@Slf4j
@UserLoginToken
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IDauService dauService;


    /**
     * Obtaining the verification Code
     *
     * @param getRandomVo
     * @return
     */
    @PostMapping("/get_random")
    @UserLoginToken(required = false)
    public ResultVo<String> getRandom(@Valid @RequestBody GetRandomVo getRandomVo) {
        String uuid = userService.getRandom(getRandomVo.getAddress());
        return ResultVo.success(uuid);
    }


    /**
     * login
     *
     * @param userLoginVo
     * @return
     */
    @PostMapping("/login")
    @UserLoginToken(required = false)
    public ResultVo<String> login(@Valid @RequestBody UserLoginVo userLoginVo) {
        String token = userService.login(userLoginVo);
        return ResultVo.success(token);
    }

    /**
     * login out
     *
     * @return
     */
    @PostMapping("/login_out")
    public ResultVo loginOut() {
        userService.loginOut();
        return ResultVo.success();
    }

    /**
     * get syn type
     *
     * @return
     */
    @GetMapping("/get_syn_type")
    public ResultVo getSynType() {
        Integer type = userService.getSynType();
        return ResultVo.success(type);
    }


    @GetMapping("/get_dau")
    @UserLoginToken(required = false)
    public ResultVo<Integer> getDau(@Valid
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt) {
        Integer count = dauService.getDau(startAt, endAt);
        return ResultVo.success(count);
    }

}
