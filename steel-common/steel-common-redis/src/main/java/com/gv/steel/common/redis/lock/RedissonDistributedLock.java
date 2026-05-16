package com.gv.steel.common.redis.lock;

import com.gv.steel.common.core.exception.LockException;
import com.gv.steel.common.core.lock.CommonLock;
import com.gv.steel.common.core.lock.DistributedLock;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * redisson 分布式锁
 */
@AllArgsConstructor
public class RedissonDistributedLock implements DistributedLock {
    private final static String LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    @Override
    public CommonLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) {
        CommonLock commonLock = getLock(key, isFair);
        RLock lock = (RLock) commonLock.getLock();
        lock.lock(leaseTime, unit);
        return commonLock;
    }

    @Override
    public CommonLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        CommonLock commonLock = getLock(key, isFair);
        RLock lock = (RLock) commonLock.getLock();
        return lock.tryLock(waitTime, leaseTime, unit) ? commonLock : null;
    }

    @Override
    public void unlock(Object lock) {
        if (lock != null) {
            if (!(lock instanceof RLock)) {
                throw new LockException("requires RLock type");
            }

            ((RLock) lock).unlock();
        }
    }

    private CommonLock getLock(String key, boolean isFair) {
        RLock lock;
        if (isFair) {
            lock = this.redissonClient.getFairLock(LOCK_PREFIX + key);
        } else {
            lock = this.redissonClient.getLock(LOCK_PREFIX + key);
        }

        return new CommonLock(lock, this);
    }
}
