package io.sinso.dataland.enums;

import lombok.Getter;

/**
 * @author hengbol
 * @date 7/23/22 4:40 PM
 */
@Getter
public enum AccountTransactionEnum {
    ETH("eth", "https://restapi.nftscan.com/api/v2/transactions/account/"),
    POLYGON("polygon", "https://polygonapi.nftscan.com/api/v2/transactions/account/"),
    BNB("BNB", "https://polygonapi.nftscan.com/api/v2/transactions/account/"),
    ;
    private String chain;

    private String url;

    AccountTransactionEnum(String chain, String url) {
        this.chain = chain;
        this.url = url;
    }

    public static String getUrl(String chain) {
        for (AccountCollectionNftEnum value : AccountCollectionNftEnum.values()) {
            if (value.getChain().equals(chain)) {
                return value.getUrl();
            }
        }
        return null;
    }
}
