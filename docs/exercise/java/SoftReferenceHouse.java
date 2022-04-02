import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置小的堆内存：-Xms20m -Xmx20m
 * 软引用，对象在程序oom时会进行软引用对象回收
 */
public class SoftReferenceHouse {
    public static void main(String[] args) {
        List<SoftReference> houses = new ArrayList<SoftReference>();
        int i = 0;
        while(true) {
            // 创建一个软引用对象，啊对对对对
            SoftReference<House> buyer = new SoftReference<House> (new House());
            houses.add(buyer);
            System.out.println("i=" + (++i));
        }
    }
}

class House {
    public House() {
    }
    public House(String string) {
    }
    private static final Integer DOOR_NUMBER = 2000;
    public Door[] doors = new Door[DOOR_NUMBER];
    class Door {}
}
