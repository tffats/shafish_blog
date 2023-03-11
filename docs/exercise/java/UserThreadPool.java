import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池使用示例
 */
public class UserThreadPool {
    public static void main(String[] args) {
        // 设置线程阻塞队列结构和长度
        BlockingQueue queue = new LinkedBlockingQueue(2);

        // 线程工厂，创建任务线程
        UserThreadFactory f1 = new UserThreadFactory("f1");
        UserThreadFactory f2 = new UserThreadFactory("f2");

        // 拒绝策略
        UserRejectHandler handler = new UserRejectHandler();

        // 利用线程工厂实例分别创建线程池
        ThreadPoolExecutor threadPoolFirst = new ThreadPoolExecutor(1,2,60,TimeUnit.SECONDS,queue,f1,handler);
        ThreadPoolExecutor threadPoolSecond = new ThreadPoolExecutor(1,2,60,TimeUnit.SECONDS,queue,f2,handler);

        Runnable task = new Task();

        for(int i=0;i<200;i++) {
            threadPoolFirst.execute(task);
            threadPoolSecond.execute(task);
        }

    }
}
