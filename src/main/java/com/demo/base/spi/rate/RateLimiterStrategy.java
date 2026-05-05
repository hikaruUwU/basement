package com.demo.base.spi.rate;

import com.demo.base.annotation.rateLimit.RateLimit;

public interface RateLimiterStrategy {
    boolean allow(String key, RateLimit rateLimit);
}
