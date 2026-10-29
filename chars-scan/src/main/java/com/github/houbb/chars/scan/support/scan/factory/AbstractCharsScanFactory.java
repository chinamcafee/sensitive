package com.github.houbb.chars.scan.support.scan.factory;

import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 抽象实现
 * @since 1.13.0
 */
public abstract class AbstractCharsScanFactory implements ICharsScanFactory {

    @Override
    public List<ICharsScan> allCharScanList() {
        List<String> scanTypeCodes = this.scanTypeList();
        if(CollectionUtil.isEmpty(scanTypeCodes)) {
            return Collections.emptyList();
        }

        List<ICharsScan> scanList = new ArrayList<ICharsScan>(scanTypeCodes.size());
        for (String scanType : scanTypeCodes) {
            scanList.add(this.getCharScan(scanType));
        }

        return scanList;
    }

}
