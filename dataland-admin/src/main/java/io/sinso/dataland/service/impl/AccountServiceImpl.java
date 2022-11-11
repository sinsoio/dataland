package io.sinso.dataland.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.enums.ResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.mapper.AccountMapper;
import io.sinso.dataland.model.Account;
import io.sinso.dataland.service.IAccountService;
import io.sinso.dataland.util.UserUtil;
import io.sinso.dataland.vo.account.AccountApplyVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @author lee
 * @since 2022-08-02
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Override
    public String applyApiKey(AccountApplyVo accountApplyVo) {
        String walletAddress = accountApplyVo.getWalletAddress();
        if (!checkAddress(walletAddress)) {
            throw new BusinessException(ResCodeEnum.WALLET_ADDRESS_ERROR);
        }
        LambdaQueryWrapper<Account> queryWrapper = Wrappers.<Account>lambdaQuery()
                .eq(Account::getWalletAddress, walletAddress);
        if (getOne(queryWrapper) != null) {
            throw new BusinessException(ResCodeEnum.ALREADY_APPLY);
        }
        String apiKey = RandomStringUtils.randomAlphabetic(12);
        Account account = new Account();
        account.setCreatedAt(LocalDateTime.now());
        account.setWalletAddress(walletAddress);
        account.setEmail(accountApplyVo.getEmail());
        account.setProjectName(accountApplyVo.getProjectName());
        account.setPurpose(accountApplyVo.getPurpose());
        account.setApiKey(apiKey);
        account.setState(1);
        save(account);
        return apiKey;
    }

    @Override
    public Account getApiKey(String token) {
        LambdaQueryWrapper<Account> queryWrapper = Wrappers.<Account>lambdaQuery()
                .eq(Account::getState, 1)
                .eq(Account::getApiKey, token);
        Account account = getOne(queryWrapper);
        if (account != null) {
            return account;
        }
        return null;
    }

    @Override
    public String refreshApiKey() {
        Integer accountId = UserUtil.getUid();
        Account account = getById(accountId);
        String apiKey = RandomStringUtils.randomAlphabetic(12);
        account.setApiKey(apiKey);
        updateById(account);
        return apiKey;
    }

    /**
     * check eth Address
     *
     * @param address
     * @return
     */
    public Boolean checkAddress(String address) {
        String pattern = "^0x[0-9a-fA-F]{40}$";
        return Pattern.matches(pattern, address);
    }
}
