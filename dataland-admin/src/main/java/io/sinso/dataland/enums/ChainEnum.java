package io.sinso.dataland.enums;

import lombok.Getter;

/**
 * @author hengbol
 * @date 7/19/22 10:02 AM
 */
@Getter
public enum ChainEnum {
    ETH("ETH", "ethereum", "https://rpc.ankr.com/eth"),
    POLYGON("MATIC", "matic", "https://polygon-rpc.com"),
    BNB("BNB", "bnb", "https://bsc-dataseed.binance.org/"),
    SINSO("SINSO", "sinso", "");

    private String chain;
    private String chainName;
    private String rpcUrl;

    ChainEnum(String chain, String chainName, String rpcUrl) {
        this.chain = chain;
        this.chainName = chainName;
        this.rpcUrl = rpcUrl;
    }

    public static String getChainName(String chain) {
        for (ChainEnum value : ChainEnum.values()) {
            if (value.getChain().equals(chain)) {
                return value.getChainName();
            }
        }
        return null;
    }

    public static String getRpcUrl(String chain) {
        for (ChainEnum value : ChainEnum.values()) {
            if (value.getChain().equals(chain)) {
                return value.getRpcUrl();
            }
        }
        return null;
    }


}
