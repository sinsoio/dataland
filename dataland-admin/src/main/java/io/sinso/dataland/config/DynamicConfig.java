package io.sinso.dataland.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lee
 * @date 2021-04-14
 */
@Data
@Component
@ConfigurationProperties(prefix = "dynamic")
public class DynamicConfig {
    /**
     * Log dynamic switch
     */
    private Boolean logSwitch;

    /**
     * File upload switch
     * true oci
     * false getway
     */
    private Boolean uploadSwitch;
}
