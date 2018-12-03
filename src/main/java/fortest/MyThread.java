package fortest;

class MyThread extends Thread {

    private int i = 0;
    @Override
    public void run(){
        Producer producer = new Producer();
        try {
            producer.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
