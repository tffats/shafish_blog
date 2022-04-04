/**
 * 实现Comparable比较器时可以指定泛型类型
 * 重写compareTo方法自定义排序
 */
public class SearchResult implements Comparable<SearchResult>{
    int relativeRatio;
    long count;
    int recentOrders;

    public SearchResult(int relativeRatio, long count) {
        this.recentOrders = recentOrders;
        this.count = count;
    }

    public void setRecentOrders(int recentOrders) {
        this.recentOrders = recentOrders;
    }

    @Override
    public int compareTo(SearchResult o) {
        if(this.relativeRatio != o.recentOrders) {
            return this.recentOrders>o.recentOrders?1:-1;
        }
        if(this.count != o.count) {
            return this.count > o.count?1:-1;
        }
        return 0;
    }
}
