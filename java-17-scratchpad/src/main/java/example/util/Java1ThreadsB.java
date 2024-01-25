package example.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Date;

public class Java1ThreadsB {

    public static void main(String[] args) {
        Java1ThreadsB example = new Java1ThreadsB();
        example.threadExample();
    }
  

    public void threadExample() {
        Runnable r = new Runnable (){
            public void run() {
                System.out.print("do something in background");;
            }
        };

        System.out.println("Heap Memory Usage: " + getHeap() );
        System.out.println("Non-Heap Memory Usage: " + getNonHeap() );
        Date start = new Date();
        for (int i=0; i<100; i++) {
            Thread t = new Thread(r);
            t.start();
        }
        Date end = new Date();
        System.out.println("Time taken with threads: " + (end.getTime() - start.getTime()) );
        System.out.println("Heap Memory Usage: " + getHeap() );
        System.out.println("Non-Heap Memory Usage: " + getNonHeap() );

        System.out.println("Heap Memory Usage: " + getHeap() );
        System.out.println("Non-Heap Memory Usage: " + getNonHeap() );
        start = new Date();
        for (int i=0; i<100; i++) {
            System.out.print("do something in foreground");
        }
        end = new Date();
        System.out.println("Time taken without threads: " + (end.getTime() - start.getTime()) );
        System.out.println("Heap Memory Usage: " + getHeap() );
        System.out.println("Non-Heap Memory Usage: " + getNonHeap() );

    }

    // Get the MemoryMXBean
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    // Get the heap memory usage
    private MemoryUsage getHeap() {return memoryMXBean.getHeapMemoryUsage(); }

    // Get the non-heap memory usage
    private MemoryUsage getNonHeap() {return memoryMXBean.getNonHeapMemoryUsage(); }

}
