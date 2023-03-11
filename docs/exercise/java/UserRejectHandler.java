import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略
 */
public class UserRejectHandler implements RejectedExecutionHandler {
    
    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        System.out.println("task rejected. "+executor.toString());
    }
    
}
