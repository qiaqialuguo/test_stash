package fortest;

class MyThread2 extends Thread {

    private int i = 0;
    @Override
    public void run(){
        Concumer concumer = new Concumer();
        try {
            concumer.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
