package com.publiccms.common.redis.hibernate.timestamper;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * Timestamper
 * 
 */
public final class Timestamper implements CacheTimestamper {

    private final AtomicLong VALUE = new AtomicLong();
    private final int BIN_DIGITS = 12;
    private final short ONE_MS = 1 << BIN_DIGITS;

    @Override
    public long next() {
        while (true) {
            long base = System.currentTimeMillis() << BIN_DIGITS;
            long maxValue = base + ONE_MS - 1;
            for (long current = VALUE.get(), update = Math.max(base, current + 1); update < maxValue; current = VALUE
                    .get(), update = Math.max(base, current + 1)) {
                if (VALUE.compareAndSet(current, update)) {
                    return update;
                }
            }
        }
    }
}
