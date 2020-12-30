package cc.laop.lang;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/7/28 15:16
 * @Description:
 */
public class ThreadLocalLearn {

    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<String> t1 = new ThreadLocal<>();
        t1.set("hello");
        System.gc();
        System.out.println(t1.get());
        test();
        System.out.println(t1.get());
    }

    static void test() {
        ThreadLocal<String> t2 = new ThreadLocal<>();
        t2.set("world");
        System.gc();
        System.out.println(t2.get());
    }

}
