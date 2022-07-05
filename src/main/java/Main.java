import com.alibaba.fastjson.JSON;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;


public class Main {
    public static void main(String args[]) throws Throwable {

        Admin admin = new Admin();
        AtomicBoolean signal = new AtomicBoolean(true);

        Producer producer = new Producer(new Integer(1).toString());

        //


        CompletableFuture<ThreadStatistics> future = new CompletableFuture();
        SendTread sendTread = new SendTread(10000L, signal, producer, future);
        Thread t = new Thread(sendTread);
        t.start();

        sleep(60000);
        signal.set(false);

        ThreadStatistics threadStatistics = future.get();
        System.out.println(JSON.toJSONString(threadStatistics));
        producer.close();
        return;
    }

}
