// For week 2
// sestoft@itu.dk * 2014-08-29

import java.util.concurrent.atomic.AtomicInteger;

class TestCountFactors {
  public static void main(String[] args) {
    // final int range = 5_000_000;
    // int count = 0;
    // for (int p=0; p<range; p++)
    //   count += countFactors(p);
    // System.out.printf("Total number of factors is %9d%n", count);
    int f = countParallelNAtomicInteger(5000000, 10); // Related to 2.1.3
    System.out.println(f);  // Related to 2.1.3
  }

    // Answer to 2.1.3
    // A lot of reuse from TestCountPrimes.java
    private static int countParallelN(int range, int threadCount) {
        final int perThread = range / threadCount;
        final MyAtomicInteger atomicInt = new MyAtomicInteger();
        
        Thread[] threads = new Thread[threadCount];
        
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            
            threads[t] = new Thread(() -> {
            for (int i=from; i<to; i++)
                atomicInt.addAndGet(countFactors(i));
            });
        }
  
        for (int t=0; t<threadCount; t++) 
            threads[t].start();
            try {
                for (int t=0; t<threadCount; t++) 
                    {threads[t].join();}
            } catch (InterruptedException exn) { }
            return atomicInt.get();
    }


    // Answer to 2.1.5
    // A lot of reuse from above ^
    private static int countParallelNAtomicInteger(int range, int threadCount) {
        final int perThread = range / threadCount;
        final AtomicInteger atomicInt = new AtomicInteger();
        
        Thread[] threads = new Thread[threadCount];
        
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            
            threads[t] = new Thread(() -> {
            for (int i=from; i<to; i++)
                atomicInt.addAndGet(countFactors(i));
            });
        }
  
        for (int t=0; t<threadCount; t++) 
            threads[t].start();
            try {
                for (int t=0; t<threadCount; t++) 
                    {threads[t].join();}
            } catch (InterruptedException exn) { }
            return atomicInt.get();
    }

  public static int countFactors(long p) {
    if (p < 2) 
      return 0;
    int factorCount = 1, k = 2;
    while (p >= k * k) {
      if (p % k == 0) {
	factorCount++;
	p /= k;
      } else 
	k++;
    }
    return factorCount;
  }
}
