package io.sinso.dataland.enums;

import lombok.Getter;

/**
 * Address of the nft saved by the user
 *
 * @author hengbol
 * @date 7/7/22 11:25 AM
 */
@Getter
public enum AccountCollectionNftEnum {
    ETH("ETH", "https://restapi.nftscan.com/api/v2/assets/"),
    POLYGON("MATIC", "https://polygonapi.nftscan.com/api/v2/assets/"),
    BNB("BNB", "https://bnbapi.nftscan.com/api/v2/assets/"),
    ;
    private String chain;

    private String url;

    AccountCollectionNftEnum(String chain, String url) {
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
