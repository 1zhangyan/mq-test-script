import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProducerThreadPoolFactory {

    private static final int corePoolSize = 10;
    private static final int maximumPoolSize = 10;
    private static final long keepAliveTime = 10;
    private static final int queueCapacity = 100000;

    public static ThreadPoolExecutor getProducerThreadPoolExecutor() {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(queueCapacity), new ThreadPoolExecutor.AbortPolicy());
    }
}
