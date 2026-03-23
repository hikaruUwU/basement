package com.demo.base.component.rate;

import com.demo.base.util.FastAccess;
import com.demo.base.annotation.RateLimit;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Component
@Fallback
public class LocalRateLimitation implements RateLimiterStrategy {
    private final ConcurrentHashMap<String, RateEntry> methodCache = new ConcurrentHashMap<>();

    @Override
    public boolean allow(String key, RateLimit rateLimit) {
        RateEntry entry = methodCache.get(key);

        if (entry == null) {
            RateEntry newEntry = new RateEntry().setPermits(rateLimit.permit()).setWindowMs(rateLimit.await());

            entry = methodCache.putIfAbsent(key, newEntry);
            if (entry == null) {
                entry = newEntry;
            }
        }
        return entry.check();
    }

    @Accessors(chain = true)
    @Setter
    @SuppressWarnings("unused")
    static class RateEntry implements Serializable {
        @Serial
        private static final long serialVersionUID = -1140929814370255779L;

        private static final VarHandle CURRENT_TIMESTAMP_HANDLE = FastAccess.bootstrapVarHandle(RateEntry.class, "currentTimestamp");
        private static final VarHandle PREVIOUS_COUNT_HANDLE = FastAccess.bootstrapVarHandle(RateEntry.class, "previousCount");
        private static final VarHandle PREVIOUS_TIMESTAMP_HANDLE = FastAccess.bootstrapVarHandle(RateEntry.class, "previousTimestamp");

        volatile long p1 = Long.MAX_VALUE, p2 = Long.MAX_VALUE, p3 = Long.MAX_VALUE, p4 = Long.MAX_VALUE, p5 = Long.MAX_VALUE, p6 = Long.MAX_VALUE;

        LongAdder currentCount = new LongAdder();

        volatile long q1 = Long.MAX_VALUE, q2 = Long.MAX_VALUE, q3 = Long.MAX_VALUE, q4 = Long.MAX_VALUE, q5 = Long.MAX_VALUE, q6 = Long.MAX_VALUE;

        private long previousCount = 0;

        volatile long r1 = Long.MAX_VALUE, r2 = Long.MAX_VALUE, r3 = Long.MAX_VALUE, r4 = Long.MAX_VALUE, r5 = Long.MAX_VALUE, r6 = Long.MAX_VALUE;

        private long previousTimestamp = System.currentTimeMillis();

        volatile long s1 = Long.MAX_VALUE, s2 = Long.MAX_VALUE, s3 = Long.MAX_VALUE, s4 = Long.MAX_VALUE, s5 = Long.MAX_VALUE, s6 = Long.MAX_VALUE;

        private long currentTimestamp = System.currentTimeMillis();

        volatile long t1 = Long.MAX_VALUE, t2 = Long.MAX_VALUE, t3 = Long.MAX_VALUE, t4 = Long.MAX_VALUE, t5 = Long.MAX_VALUE, t6 = Long.MAX_VALUE;

        long permits;
        long windowMs;

        protected boolean check() {
            long now = System.currentTimeMillis();
            long W = windowMs;
            long L = permits;

            long T_current_snapshot = (long) CURRENT_TIMESTAMP_HANDLE.getVolatile(this);


            if (now > T_current_snapshot + W) {
                if (CURRENT_TIMESTAMP_HANDLE.compareAndSet(this, T_current_snapshot, now)) {
                    long C_prev = currentCount.sumThenReset();
                    PREVIOUS_COUNT_HANDLE.setRelease(this, C_prev);
                    PREVIOUS_TIMESTAMP_HANDLE.setRelease(this, T_current_snapshot);
                }
            }

            long T_latest = (long) CURRENT_TIMESTAMP_HANDLE.getVolatile(this);

            long C_prev_vol = (long) PREVIOUS_COUNT_HANDLE.getAcquire(this);

            long C_current = currentCount.sum();

            long passedTimeInCurrentWindow = now - T_latest;

            double P = Math.max(0.0, Math.min(1.0, (double) passedTimeInCurrentWindow / W));

            long estimatedCount = (long) (C_prev_vol * (1.0 - P)) + C_current;

            if (estimatedCount + 1 > L) return false;

            currentCount.increment();
            return true;
        }
    }
}