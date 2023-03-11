import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 使用countDownLatch基于执行时间的信号量进行线程同步
 * countDownLatch能够使一个线程等待其他线程完成各自的工作后再执行
 * 主线程任务需要等待其他线程执行完成后再操作的场景下使用
 */
public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch count = new CountDownLatch(3);

        Thread a = new TranslateThread("1", count);
        Thread b = new TranslateThread("2", count);
        Thread c = new TranslateThread("3", count);
        Thread d = new TranslateThread("4", count);
        Thread f = new TranslateThread("5", count);

        a.start();
        b.start();
        c.start();
        d.start();
        f.start();

        try {
            // 在await中传入时间就可以控制主线程等待的时间
            // count.await(3, TimeUnit.SECONDS);
            count.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("all done");
    }
}

class TranslateThread extends Thread {
    private String content;
    private final CountDownLatch count;

    public TranslateThread(String content, CountDownLatch count) {
        this.content = content;
        this.count = count;
    }

    @Override
    public void run() {
        /* if(Math.random() > 0.5) {
            throw new RuntimeException("error");
        } */
         try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        System.out.println(content+"阶段执行完成");
        count.countDown();
    }
}