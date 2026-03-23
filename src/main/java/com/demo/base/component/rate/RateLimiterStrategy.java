package com.demo.base.component.rate;

import com.demo.base.annotation.RateLimit;

public interface RateLimiterStrategy {
    boolean allow(String key, RateLimit rateLimit);
}
