import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocal 的简单用法，可将对象设为共享变量，统一设置初始值，但每个线程对该值的修改都互相独立
 */
public class CsGameByThreadLocal {
    // 游戏子弹数
    private static final Integer BULLET_NUMBR = 1500;
    // 杀敌数
    private static final Integer KILLED_ENEMIES = 0;
    // 命
    private static final Integer LIFE_VALUE = 10;

    private static final Integer TOTAL_PLAYERS = 10;

    // 每个线程的随机数生成器
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private static final ThreadLocal<Integer> BULLET_NUMBER_THREADLOCAL = new ThreadLocal<Integer>() {
        // 线程调用ThreadLocal.get()时执行
        @Override
        protected Integer initialValue() {
            return BULLET_NUMBR;
        }
    };

    private static final ThreadLocal<Integer> KILLED_ENEMIES_THREADLOCAL = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return KILLED_ENEMIES;
        }
    };

    private static final ThreadLocal<Integer> LIFE_VALUE_THREADLOCAL = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return LIFE_VALUE;
        }
    };
    
    private static class Player extends Thread {
        @Override
        public void run() {
            Integer bullets = BULLET_NUMBER_THREADLOCAL.get() - RANDOM.nextInt(BULLET_NUMBR);
            Integer killEnemies = KILLED_ENEMIES_THREADLOCAL.get() - RANDOM.nextInt(TOTAL_PLAYERS/2);
            Integer lifeValue = LIFE_VALUE_THREADLOCAL.get() - RANDOM.nextInt(LIFE_VALUE);

            System.out.println(getName() + " bullet_number is"+bullets);
            System.out.println(getName() + " killed_enemies is"+killEnemies);
            System.out.println(getName() + " life_value is"+lifeValue);

            BULLET_NUMBER_THREADLOCAL.remove();
            KILLED_ENEMIES_THREADLOCAL.remove();
            LIFE_VALUE_THREADLOCAL.remove();
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<TOTAL_PLAYERS;i++) {
            new Player().start();
        }
    }
}
