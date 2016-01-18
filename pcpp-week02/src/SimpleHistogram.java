// For week 2
// sestoft@itu.dk * 2014-09-04

import java.util.concurrent.atomic.*;

class SimpleHistogram {
  public static void main(String[] args) {
//    final Histogram2 histogram = new Histogram2(30);
//    histogram.increment(7);
//    histogram.increment(13);
//   histogram.increment(7);
//    dump(histogram);

    final Histogram5 hist = new Histogram5(30);
    hist.countParallelNPrimeFactors(5000000, 10);
    dump(hist);

    for (int i : hist.getBins()) {
   		System.out.println(i); 	
    }
//    System.out.println(hist.getBins());
  }

  public static void dump(Histogram histogram) {
    int totalCount = 0;
    for (int bin=0; bin<histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
      totalCount += histogram.getCount(bin);
    }
    System.out.printf("      %9d%n", totalCount);
  }
}

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public int getSpan();
  public int[] getBins();
}

class Histogram1 {
  private int[] counts;
  public Histogram1(int span) {
    this.counts = new int[span];
  }
  public void increment(int bin) {
    counts[bin] = counts[bin] + 1;
  }
  public int getCount(int bin) {
    return counts[bin];
  }
  public int getSpan() {
    return counts.length;
  }
}

// Partial answer to 2.3.1
class Histogram2 implements Histogram {
  private final int[] counts;
  
  public Histogram2(int span) {
    this.counts = new int[span];
  }
  public synchronized void increment(int bin) {
    counts[bin] = counts[bin] + 1;
  }
  public synchronized int getCount(int bin) {
    return counts[bin];
  }
  public int getSpan() {
    return counts.length;
  }

  // 2.3.5
  public synchronized int[] getBins() {
  	return counts.clone();
  }

  // 2.3.2
  public void countParallelNPrimeFactors(int range, int threadCount) {
        final int perThread = range / threadCount;
        
        Thread[] threads = new Thread[threadCount];
        
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            
            threads[t] = new Thread(() -> {
            for (int i=from; i<to; i++)
                increment(TestCountFactors.countFactors(i));
            });
        }
  
        for (int t=0; t<threadCount; t++) 
            threads[t].start();
            try {
                for (int t=0; t<threadCount; t++) 
                    {threads[t].join();}
            } catch (InterruptedException exn) { }
    }

}

// 2.3.3
class Histogram3 implements Histogram {
  private final AtomicInteger[] counts;
  
  public Histogram3(int span) {
    this.counts = new AtomicInteger[span];
    for (int i = 0; i < counts.length; i++) {
    	counts[i] = new AtomicInteger(0);
    }
  }
  public void increment(int bin) {
  	counts[bin].incrementAndGet();
  }
  public int getCount(int bin) {
    return counts[bin].get();
  }
  public int getSpan() {
    return counts.length;
  }

  // 2.3.5
  public synchronized int[] getBins() {
  	int[] bins = new int[counts.length];
  	for (int i = 0; i < counts.length; i++) {
  		bins[i] = counts[i].get();
  	}
  	return bins;
  }

  public void countParallelNPrimeFactors(int range, int threadCount) {
        final int perThread = range / threadCount;
        
        Thread[] threads = new Thread[threadCount];
        
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            
            threads[t] = new Thread(() -> {
            for (int i=from; i<to; i++)
                increment(TestCountFactors.countFactors(i));
            });
        }
  
        for (int t=0; t<threadCount; t++) 
            threads[t].start();
            try {
                for (int t=0; t<threadCount; t++) 
                    {threads[t].join();}
            } catch (InterruptedException exn) { }
    }

}

// 2.3.4
class Histogram4 implements Histogram {
  private final AtomicIntegerArray counts;
  
  public Histogram4(int span) {
    this.counts = new AtomicIntegerArray(span);
  }
  public void increment(int bin) {
  	counts.addAndGet(bin, 1);
  }
  public int getCount(int bin) {
    return counts.get(bin);
  }
  public int getSpan() {
    return counts.length();
  }

  // 2.3.5
  public synchronized int[] getBins() {
  	int[] bins = new int[counts.length()];
  	for (int i = 0; i < counts.length(); i++) {
  		bins[i] = counts.get(i);
  	}
  	return bins;
  }

  public void countParallelNPrimeFactors(int range, int threadCount) {
        final int perThread = range / threadCount;
        
        Thread[] threads = new Thread[threadCount];
        
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            
            threads[t] = new Thread(() -> {
            for (int i=from; i<to; i++)
                increment(TestCountFactors.countFactors(i));
            });
        }
  
        for (int t=0; t<threadCount; t++) 
            threads[t].start();
            try {
                for (int t=0; t<threadCount; t++) 
                    {threads[t].join();}
            } catch (InterruptedException exn) { }
    }

}

class Histogram5 implements Histogram {
  private final LongAdder[] counts;
  
  public Histogram5(int span) {
    this.counts = new LongAdder[span];
    for (int i = 0; i < counts.length; i++) {
      counts[i] = new LongAdder();
    }
  }
  public void increment(int bin) {
    counts[bin].increment();
  }
  public int getCount(int bin) {
    return counts[bin].intValue();
  }
  public int getSpan() {
    return counts.length;
  }

  // 2.3.5
  public synchronized int[] getBins() {
    int[] bins = new int[counts.length];
    for (int i = 0; i < counts.length; i++) {
      bins[i] = counts[i].intValue();
    }
    return bins;
  }

  public void countParallelNPrimeFactors(int range, int threadCount) {
        final int perThread = range / threadCount;
        
        Thread[] threads = new Thread[threadCount];
        
        for (int t=0; t<threadCount; t++) {
            final int from = perThread * t, 
            to = (t+1==threadCount) ? range : perThread * (t+1); 
            
            threads[t] = new Thread(() -> {
            for (int i=from; i<to; i++)
                increment(TestCountFactors.countFactors(i));
            });
        }
  
        for (int t=0; t<threadCount; t++) 
            threads[t].start();
            try {
                for (int t=0; t<threadCount; t++) 
                    {threads[t].join();}
            } catch (InterruptedException exn) { }
    }

}