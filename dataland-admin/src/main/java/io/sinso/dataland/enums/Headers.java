package io.sinso.dataland.enums;

import lombok.Getter;

/**
 * @author lee
 * @date 2020/1/19
 */
@Getter
public enum Headers {
    // token
    TOKEN("Authorization"),
    CLIENT_TYPE("CLIENT-TYPE"),
    ;

    private String header;

    Headers(String header) {
        this.header = header;
    }

}
