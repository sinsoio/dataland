package io.sinso.dataland.vo.file;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hengbol
 * @date 10/27/22 4:01 PM
 */
@Data
public class ChainGetDataDto {
    private String tokenUri;
    private String chainGetStateFailMsg;
    private LocalDateTime chainGetStateFailAt;
    /**
     * true Achieve success
     */
    private Boolean state;
    private String imageUrl;
    private String attributes;
}
