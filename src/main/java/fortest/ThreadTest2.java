package fortest;

public class ThreadTest2 {

    public static void main(String[] args) {


        Thread myThread1 = new MyThread2();     // 创建一个新的线程  myThread1  此线程进入新建状态
        Thread myThread2 = new MyThread2();
        Thread myThread3 = new MyThread2();     // 创建一个新的线程  myThread1  此线程进入新建状态
        Thread myThread4 = new MyThread2();
        Thread myThread5 = new MyThread2();
        Thread myThread6 = new MyThread2();
        Thread myThread7 = new MyThread2();
        Thread myThread8 = new MyThread2();
//        Thread myThread9 = new MyThread2();
//        Thread myThread10 = new MyThread2();
//        Thread myThread11 = new MyThread2();
//        Thread myThread12 = new MyThread2();
//        Thread myThread13 = new MyThread2();
//        Thread myThread14 = new MyThread2();
//        Thread myThread15 = new MyThread2();
//        Thread myThread16 = new MyThread2();// 创建一个新的线程 myThread2 此线程进入新建状态
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