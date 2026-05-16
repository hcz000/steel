package com.gv.steel.common.core.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonLock implements AutoCloseable {
    private final Object lock;

    private final DistributedLock locker;

    @Override
    public void close() throws Exception {
        this.locker.unlock(this.lock);
    }
}
