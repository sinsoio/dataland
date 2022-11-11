package io.sinso.dataland.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.sinso.dataland.model.User;
import io.sinso.dataland.vo.user.UserLoginVo;

import java.time.LocalDateTime;

/**
 * @author lee
 * @since 2022-06-13
 */
public interface IUserService extends IService<User> {

    User getToken(String token);

    String getRandom(String address);

    String login(UserLoginVo userLoginVo);

    void loginOut();

    Integer getSynType();

    User getOneByAddress(String address);


    /**
     * Get the number of new users
     *
     * @param max
     * @param min
     * @return
     */
    Integer getNumAddress(LocalDateTime max, LocalDateTime min);
}
