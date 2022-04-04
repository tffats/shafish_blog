import java.util.Comparator;

/**
 * 除了直接在SearchResult类中重写compareTo，还可以定义一个外部的比较器，避免直接修改SearchResult代码
 * Arrays.sort(T[] a, Comparator<? super T> c)
 */
public class SearchResultComparator implements Comparator<SearchResult>{

    @Override
    public int compare(SearchResult o1, SearchResult o2) {
        // TODO Auto-generated method stub
        if(o1.relativeRatio != o2.relativeRatio) {
            return o1.relativeRatio > o2.relativeRatio?1:-1;
        }
        if(o1.recentOrders != o2.recentOrders) {
            return o1.recentOrders > o2.recentOrders?1:-1;
        }
        if(o1.count != o2.count) {
            return o1.count>o2.count?1:-1;
        }
        return 0;
    }
    
}
