import java.lang.reflect.Field;

/**
 * 使用反射修改对象变量的demo
 */
public class ClassTest {
    private static int[] array = new int[3];
    private static int length = array.length;

    private static Class<One> one = One.class;
    private static Class<Another> another = Another.class;

    public static void main(String[] args) throws Exception {

        // 只能调用无参的构造方法
        One oneObject = one.getDeclaredConstructor().newInstance();
        oneObject.call();

        // new为强类型校验，可调用任何构造方法
        Another anotherObject = new Another();
        anotherObject.speak();

        // 获取成员属性
        Field privateFieldInOne = one.getDeclaredField("inner");
        privateFieldInOne.setAccessible(true);
        privateFieldInOne.set(oneObject, "world changed");

        System.out.println(oneObject.getInner());
    }
 }

class One {
    private String inner = "time files";

    public void call() {
        System.out.println("hello world");
    }

    public String getInner() {
        return inner;
    }
}

class Another {
    public void speak() {
        System.out.println("easy coding");
    }
}
