package io.sinso.dataland.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.enums.AccountTransactionEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.mapper.RecordMapper;
import io.sinso.dataland.model.Record;
import io.sinso.dataland.service.IFileCollectionService;
import io.sinso.dataland.service.IRecordService;
import io.sinso.dataland.service.IScanConfigService;
import io.sinso.dataland.util.LocalDateUtils;
import io.sinso.dataland.vo.file.NftScanRpcGetResVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author lee
 * @since 2022-06-27
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Autowired
    private IScanConfigService scanConfigService;

    @Autowired
    private IFileCollectionService fileCollectionService;


    @Override
    public Record findOne(Integer uid, String chain, String blockNumber) {
        LambdaQueryWrapper<Record> queryWrapper = Wrappers.<Record>lambdaQuery()
                .eq(Record::getUserId, uid)
                .eq(Record::getBlockNumber, blockNumber)
                .eq(Record::getChain, chain);
        return getOne(queryWrapper);
    }


    @Override
    public void addUserRecordByUserAddress(String address, Integer uid) {
        for (AccountTransactionEnum obj : AccountTransactionEnum.values()) {
            String cursor = "";
            Integer pageSize = 100;
            do {
                try {
                    String url = obj.getUrl() + address + "?&cursor=" + cursor + "&limit=" + pageSize;
                    NftScanRpcGetResVo nftScanRpcGetResVo = scanConfigService.getRequestList(url);
                    if (nftScanRpcGetResVo != null) {
                        List<Map> content = nftScanRpcGetResVo.getList();
                        if (content.size() == 0) {
                            return;
                        }
                        cursor = nftScanRpcGetResVo.getNext();
                        for (Map t : content) {
                            String blockNumber = t.get("block_number").toString();
                            Record recordOne = findOne(uid, obj.getChain(), blockNumber);
                            if (recordOne != null) {
                                continue;
                            }
                            String txReceiveAddress = t.get("receive").toString();
                            Record record = new Record();
                            record.setCreatedAt(LocalDateTime.now());
                            record.setTxFromAddress(t.get("from").toString());
                            record.setTxToAddress(t.get("to").toString());
                            record.setTxSendAddress(t.get("send").toString());
                            record.setTxReceiveAddress(txReceiveAddress);
                            record.setTxTokenId(t.get("token_id").toString());
                            Long transactionTime = Long.valueOf(t.get("timestamp").toString());
                            LocalDateTime txTime = LocalDateUtils.timestampToLocalDateTime(transactionTime);
                            record.setTxTime(txTime);
                            record.setBlockNumber(blockNumber);
                            record.setBlockHash(t.get("block_hash").toString());
                            record.setTxHash(t.get("hash").toString());
                            record.setChain(obj.getChain());
                            record.setType(txReceiveAddress.equals(address) ? 1 : 2);
                            record.setState(false);
                            record.setUserId(uid);
                            save(record);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (StringUtils.isNotBlank(cursor));
        }

    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void updateUserNft(String address, Integer uid) {
        LambdaQueryWrapper<Record> queryWrapper = Wrappers.<Record>lambdaQuery()
                .eq(Record::getUserId, uid)
                .eq(Record::getTxSendAddress, address);
        List<Record> list = list(queryWrapper);
        list.forEach(record -> {
            fileCollectionService.removeFileByTxTokenId(record.getTxTokenId());
            record.setState(true);
            updateById(record);
        });

    }
}
