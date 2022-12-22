package com.publiccms.common.tools;

/** Copyright 2010-2012 Twitter, Inc.*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An object that generates IDs. This is broken into a separate class in case we
 * ever want to support multiple worker threads per process
 */
public class IdWorker {
    protected final Log log = LogFactory.getLog(getClass());
    private long workerId;

    private long sequence = 0L;

    private long workerIdBits = 10L;

    private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    private long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;

    private long timestampLeftShift = sequenceBits + workerIdBits;

    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    public IdWorker(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(new StringBuilder("worker Id can't be greater than ").append(maxWorkerId)
                    .append(" or less than 0").toString());
        }
        this.workerId = workerId;
        log.info(new StringBuilder("worker starting. timestamp left shift ").append(timestampLeftShift)
                .append(", worker id bits ").append(workerIdBits).append(", sequence bits ").append(sequenceBits)
                .append(", workerid ").append(workerId).toString());
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * 
     * @param lastTimestamp
     *            上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            log.error(new StringBuilder("clock is moving backwards. Rejecting requests until ").append(lastTimestamp).toString());
            throw new RuntimeException(new StringBuilder("Clock moved backwards. Refusing to generate id for ")
                    .append(lastTimestamp - timestamp).append(" milliseconds").toString());
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return (timestamp << timestampLeftShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * 
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

}