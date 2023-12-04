package example;

public class Java1ThreadsA {

    public void threadExample() {
        Runnable r = new Runnable (){
            public void run() {
                System.out.println("do something in background");;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

}
