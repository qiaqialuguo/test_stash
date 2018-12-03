package fortest;

public class ThreadTest {

    public static void main(String[] args) {


        Thread myThread1 = new MyThread();     // 创建一个新的线程  myThread1  此线程进入新建状态
        Thread myThread2 = new MyThread();
        Thread myThread3 = new MyThread();     // 创建一个新的线程  myThread1  此线程进入新建状态
        Thread myThread4 = new MyThread();
        Thread myThread5 = new MyThread();
        Thread myThread6 = new MyThread();
        Thread myThread7 = new MyThread();
        Thread myThread8 = new MyThread();
//        Thread myThread9 = new MyThread();
//        Thread myThread10 = new MyThread();
//        Thread myThread11 = new MyThread();
//        Thread myThread12 = new MyThread();
//        Thread myThread13 = new MyThread();
//        Thread myThread14 = new MyThread();
//        Thread myThread15 = new MyThread();
//        Thread myThread16 = new MyThread();// 创建一个新的线程 myThread2 此线程进入新建状态
        myThread1.start();                     // 调用start()方法使得线程进入就绪状态
        myThread2.start();
        myThread3.start();                     // 调用start()方法使得线程进入就绪状态
        myThread4.start();
        myThread5.start();
        myThread6.start();
        myThread7.start();
        myThread8.start();
//        myThread9.start();
//        myThread10.start();
//        myThread11.start();
//        myThread12.start();
//        myThread13.start();
//        myThread14.start();
//        myThread15.start();
//        myThread16.start();// 调用start()方法使得线程进入就绪状态


    }
}