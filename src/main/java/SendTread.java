import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class SendTread implements Runnable {

    private CompletableFuture<ThreadStatistics> futureTask;

    private Producer producer;

    private AtomicBoolean signal;

    private Long rateLimiterValue;

    public SendTread(Long rateLimiterValue, AtomicBoolean signal, Producer producer, CompletableFuture future) {
        this.futureTask = future;
        this.signal = signal;
        this.rateLimiterValue = rateLimiterValue;
        this.producer = producer;
    }

    @Override
    public void run() {
        RateLimiter rateLimiter = RateLimiter.create(rateLimiterValue);
        Long startTime = System.currentTimeMillis();
        Long sendSuccess = 0L;
        Long sendFail = 0L;
        while(signal.get()) {
            rateLimiter.acquire();
            if (producer.send()) {
                sendSuccess ++;
            } else {
                sendFail ++;
            }
        }
        Long endTime = System.currentTimeMillis();
        ThreadStatistics threadStatistics = new ThreadStatistics();
        threadStatistics.setSendFailTotal(sendFail);
        threadStatistics.setSendSuccessTotal(sendSuccess);
        threadStatistics.setMillisTotal(endTime - startTime);
        threadStatistics.setRateLimitValue(rateLimiterValue);
        futureTask.complete(threadStatistics);
    }

}
