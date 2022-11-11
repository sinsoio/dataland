package io.sinso.dataland.vo.account;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hengbol
 * @date 8/3/22 11:22 AM
 */
@Data
public class NftDetailApiPageVo {

    /**
     * fileUrl
     */
    private String fileUrl;

    /**
     * name
     */
    private String name;

    /**
     * contentType
     */
    private String contentType;

    /**
     * mintDate
     */
    private LocalDateTime mintDate;

    /**
     * mintHash
     */
    private String mintHash;

    /**
     * holder
     */
    private String holder;

}
