package io.sinso.dataland.enums;

import lombok.Getter;

/**
 * The address of the nft initialized by the user
 *
 * @author hengbol
 * @date 7/7/22 11:25 AM
 */
@Getter
public enum AccountGetAllNftEnum {
    ETH_721("ETH", "https://restapi.nftscan.com/api/v2/account/own/", "erc721"),
    ETH_1155("ETH", "https://restapi.nftscan.com/api/v2/account/own/", "erc1155"),
    POLYGON_721("MATIC", "https://polygonapi.nftscan.com/api/v2/account/own/", "erc721"),
    POLYGON_1155("MATIC", "https://polygonapi.nftscan.com/api/v2/account/own/", "erc1155"),
    BSC_721("BNB", "https://bnbapi.nftscan.com/api/v2/account/own/", "erc721"),
    BSC_1155("BNB", "https://bnbapi.nftscan.com/api/v2/account/own/", "erc1155"),
    ;
    private String chain;

    private String url;

    private String standerd;

    AccountGetAllNftEnum(String chain, String url, String standerd) {
        this.chain = chain;
        this.url = url;
        this.standerd = standerd;
    }
}
