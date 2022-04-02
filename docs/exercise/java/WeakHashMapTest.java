import java.util.WeakHashMap;

/**
 * 弱引用，保存缓存不敏感的临时数据
 * 适当使用引用可以减少对象在生命周期中所占用的内存大小
 */
public class WeakHashMapTest {
    public static void main(String[] args) {
        House seller1 = new House("one");
        SellerInfo sellerInfo1 = new SellerInfo();

        House seller2 = new House("two");
        SellerInfo sellerInfo2 = new SellerInfo();

        WeakHashMap<House, SellerInfo> weakHashMap = new WeakHashMap<House,SellerInfo>();
        weakHashMap.put(seller1, sellerInfo1);
        weakHashMap.put(seller2, sellerInfo2);

        System.out.println("weakHashMap before null:"+weakHashMap.size());

        // 回收指向的引用
        seller1 = null;

        System.gc();
        System.runFinalization();

        System.out.println("weakHashMap before null:"+weakHashMap.size());

        System.out.println(weakHashMap);

    }

}

class SellerInfo {}