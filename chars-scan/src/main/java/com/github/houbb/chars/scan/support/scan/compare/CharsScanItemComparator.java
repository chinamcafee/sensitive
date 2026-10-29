package com.github.houbb.chars.scan.support.scan.compare;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;

import java.util.Comparator;

/**
 * 比较器
 *
 * 匿名内部类，即使使用单例，也会导致创建一个 xx$1 的 class
 *
 * @since 1.17.0
 */
public class CharsScanItemComparator implements Comparator<CharsScanMatchItem> {

    @Override
    public int compare(CharsScanMatchItem o1, CharsScanMatchItem o2) {
        //1. 先使用开始下标
        int startIndexDiff = o1.getStartIndex() - o2.getStartIndex();
        if(startIndexDiff != 0) {
            return startIndexDiff;
        }
        //2. 如果相同，则看优先级
        return o1.getPriority() - o2.getPriority();
    }

}
