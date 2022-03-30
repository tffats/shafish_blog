import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 任务线程工厂，可通过newThread快速创建线程任务
 * ps：额外添加调用说明，业务含义等信息
 */
public class UserThreadFactory implements ThreadFactory {

    private final String namePrefix;
    private final AtomicInteger nextId = new AtomicInteger(1);

    UserThreadFactory(String whatFeatureOfGroup) {
        namePrefix = "UserThreadFactory's "+whatFeatureOfGroup+"-worker-";
    }

    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix+nextId.getAndIncrement();
        Thread thread = new Thread(null, task, name,0,false);
        System.out.println(thread.getName());
        return thread;
    }
}

class Task implements Runnable {
    private final AtomicLong count = new AtomicLong(0L);
    
    @Override
    public void run() {
        System.out.println("running-"+count.getAndIncrement());
    }
}
