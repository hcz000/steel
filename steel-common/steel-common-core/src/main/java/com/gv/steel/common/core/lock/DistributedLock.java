package com.gv.steel.common.core.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {
    CommonLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;

    default CommonLock lock(String key, long leaseTime, TimeUnit unit) throws Exception {
        return this.lock(key, leaseTime, unit, false);
    }

    default CommonLock lock(String key, boolean isFair) throws Exception {
        return this.lock(key, -1L, (TimeUnit) null, isFair);
    }

    default CommonLock lock(String key) throws Exception {
        return this.lock(key, -1L, (TimeUnit) null, false);
    }

    CommonLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;

    default CommonLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws Exception {
        return this.tryLock(key, waitTime, leaseTime, unit, false);
    }

    default CommonLock tryLock(String key, long waitTime, TimeUnit unit, boolean isFair) throws Exception {
        return this.tryLock(key, waitTime, -1L, unit, isFair);
    }

    default CommonLock tryLock(String key, long waitTime, TimeUnit unit) throws Exception {
        return this.tryLock(key, waitTime, -1L, unit, false);
    }

    void unlock(Object lock) throws Exception;

    default void unlock(CommonLock commonLock) throws Exception {
        if (commonLock != null) {
            this.unlock(commonLock.getLock());
        }

    }
}
