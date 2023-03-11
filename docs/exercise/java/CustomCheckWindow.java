import java.util.concurrent.Semaphore;

/**
 * 基于Semaphore空闲信号的信号量
 */
public class CustomCheckWindow {
    public static void main(String[] args) {
        // 设置三个信号量可用
        Semaphore semaphore = new Semaphore(3);

        // 根据分配的信号量是否空闲开始处理
        for(int i=1;i<6;i++) {
            new SecurityCheckThread(i, semaphore).start();
        }
    }

    private static class SecurityCheckThread extends Thread {
        private int seq;
        private Semaphore semaphore;

        public SecurityCheckThread(int seq,Semaphore semaphore) {
            this.seq = seq;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println(seq+"处理中");
                if(seq % 2 == 0) {
                    Thread.sleep(1000);
                    System.out.println(seq+"动作可疑");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
                System.out.println(seq+"完成处理");
            }
        }
    }
}
