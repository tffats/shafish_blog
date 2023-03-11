import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在线程池中使用threadLocal容易产生脏数据：
 * 线程池中的线程会重用Thread对象，与Thread绑定的ThreadLocal变量也会被重用，如果在run()中没有显式调用remove()进行相关threadLocal清理，
 * 当下一个线程没有进行set操作，而是直接get时，就会重用上一个线程的数据。
 * 所以每次使用threadLocal时记得进行remove
 */
public class DirtyDataInThreadLocal {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<String> ();

    public static void main(String[] args) {
        ExecutorService pool  = Executors.newFixedThreadPool(1);
        for(int i=0;i<2;i++) {
            Mythread thread = new Mythread();
            pool.execute(thread);
        }
    }

    private static class Mythread extends Thread {
        private static boolean flag = true;
        @Override
        public void run() {
            if(flag) {
                // 开始执行的线程进行set操作后没有remove，之后的现场因为flag为false，不会再次set，就会造成数据重用。
                threadLocal.set(this.getName()+" info ");
                flag = false;
            }
            // threadLocal.remove();
            System.out.println(this.getName()+" 线程是："+threadLocal.get());
        }
    }
}
