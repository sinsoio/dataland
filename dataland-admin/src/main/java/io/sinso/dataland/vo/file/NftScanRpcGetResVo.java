package io.sinso.dataland.vo.file;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hengbol
 * @date 7/25/22 9:55 AM
 */
@Data
public class NftScanRpcGetResVo {
    private List<Map> list;
    private String next;
}
