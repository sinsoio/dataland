package io.sinso.dataland.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.sinso.dataland.model.Account;
import io.sinso.dataland.vo.account.AccountApplyVo;

/**
 * @author lee
 * @since 2022-08-02
 */
public interface IAccountService extends IService<Account> {

    /**
     * applyApiKey
     *
     * @param accountApplyVo
     * @return
     */
    String applyApiKey(AccountApplyVo accountApplyVo);

    /**
     * getApiKey
     *
     * @param token
     * @return
     */
    Account getApiKey(String token);


    /**
     * refreshApiKey
     *
     * @return
     */
    String refreshApiKey();
}
