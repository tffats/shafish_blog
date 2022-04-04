import java.util.ArrayList;
import java.util.List;

/**
 * 集合与泛型
 * 因为list等集合没有类型和赋值等的使用限制，如果乱用就会发生类型转换失败的情况
 * 集合中使用泛型就能很好明确元素类型
 */
public class ListNoGeneric {
    public static void main(String[] args) {
        // 没有添加泛型限制的list使用
        List a1 = new ArrayList();
        a1.add(new Object());
        a1.add(Integer.valueOf(11));
        a1.add(new String("hello world a1"));

        // 添加Object泛型限制
        List<Object> a2 = a1;
        a2.add(new Object());
        a2.add(Integer.valueOf(11));
        a2.add(new String("hello world a2"));

        List<Integer> a3 = a1;
        a3.add(Integer.valueOf(11));
        // a3.add(new Object()); // 此时会编译报错，提示参数为非Integer类型

        // ？表示可以接受任何类型的集合引用赋值，但不能添加任何元素 ！！
        List<?> a4 = a1;
        a1.remove(0);
        a4.clear();
        // a4.add(new Object()); // 此时会编译报错，不允许添加任何元素

    }
}
