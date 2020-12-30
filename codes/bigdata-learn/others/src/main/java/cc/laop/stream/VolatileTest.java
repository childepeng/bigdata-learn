package cc.laop.stream;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/7/21 18:09
 * @Description:
 */
public class VolatileTest {

    public static void main(String[] args) {
        Pao p = new Pao();
        p.start();
        while (true) {
            if (p.isFlag()) {
                System.out.println("有点东西");
            }
        }
    }


    public static class Pao extends Thread {

        private volatile boolean flag = false;

        public boolean isFlag() {
            return flag;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            System.out.println(flag);
        }
    }


}
