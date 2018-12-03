package fortest;

public class TestThread5 extends Thread {
    private String name;
    public TestThread5(String name) {
        super(name);
        this.name = name;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程运行开始 ");
        for (int i = 0; i < 5; i++) {
            System.out.println("子线程" + name + "运行： " + i);
            try {
                sleep((int) Math.random() * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "线程运行结束");
    }
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "主线程运行开始!");
        TestThread5 mTh1 = new TestThread5("A");
        TestThread5 mTh2 = new TestThread5("B");
        mTh1.start();
        mTh2.start();
        try{
            mTh1.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        try{
            mTh2.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "主线程运行结束!");
    }
}