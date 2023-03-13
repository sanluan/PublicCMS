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
            throw new IllegalArgumentException(
                    CommonUtils.joinString("worker Id can't be greater than ", maxWorkerId, " or less than 0"));
        }
        this.workerId = workerId;
        log.info(CommonUtils.joinString("worker starting. timestamp left shift ", timestampLeftShift, ", worker id bits ", workerIdBits,
                ", sequence bits ", sequenceBits, ", workerid ", workerId));
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
            log.error(CommonUtils.joinString("clock is moving backwards. Rejecting requests until ", lastTimestamp));
            throw new RuntimeException(CommonUtils.joinString("Clock moved backwards. Refusing to generate id for ",
                    lastTimestamp - timestamp, " milliseconds"));
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