package com.github.houbb.chars.scan.support.core;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 核心实现-线程池尝试
 *
 * 为何可能存在 thread 并发时存在 null 元素?
 *
 * 应该是 list 并发添加导致，使用 {@link  java.util.concurrent.CopyOnWriteArrayList} 替换。
 * @author d
 * @since 1.6.0
 */
public class CharsCoreConcurrency extends CharsCoreThreadLocal {

    /**
     * 线程池
     */
    private final ExecutorService EXECUTOR_SERVICE;

    public CharsCoreConcurrency(int threadSize) {
        /**
         * 线程大小
         *
         * 说明：一般也不适合太大。
         */

        //TODO：这里后续可以考虑修改一下名称之类的。
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(threadSize);
    }

    public CharsCoreConcurrency() {
        this(10);
    }

    @Override
    protected List<CharsScanMatchItem> doScan(final char[] chars,
                                              List<ICharsScan> charsScanList,
                                              final CharsScanContext context) {
        // 避免并发安全
        final List<CharsScanMatchItem> resultList = new CopyOnWriteArrayList<CharsScanMatchItem>();

        // 遍历
        try {
            final int scanSize = charsScanList.size();
            if(scanSize > 1) {
                final CountDownLatch countDownLatch = new CountDownLatch(scanSize);
                // 扫描
                for(final ICharsScan charsScan : charsScanList) {
                    EXECUTOR_SERVICE.execute(new Runnable() {
                        @Override
                        public void run() {
                            // 执行
                            scanTask(chars, charsScan, context, resultList);

                            countDownLatch.countDown();
                        }
                    });
                }
                // 加锁等待
                countDownLatch.await();
            } else {
                scanTask(chars, CollectionUtil.getFirst(charsScanList), context, resultList);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return resultList;
    }



    private void scanTask(final char[] chars, final ICharsScan charsScan,
                          final CharsScanContext context,
                          final List<CharsScanMatchItem> resultList) {
        // 多线程执行
        for(int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];
            charsScan.scan(i, currentChar, chars, context);
        }

        // 需要判空
        List<CharsScanMatchItem> itemList = charsScan.getMatchList();
        if(CollectionUtil.isNotEmpty(itemList)) {
            resultList.addAll(itemList);
        }
    }

}
