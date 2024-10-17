package com.hrmanagementsystem.dao.interfaces;

import java.util.concurrent.TimeUnit;

public interface Scheduler {
    void schedule(Runnable command, long delay, TimeUnit unit);
}
