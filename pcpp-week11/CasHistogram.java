import java.util.concurrent.atomic.AtomicInteger;

interface Histogram {
	void increment(int bin);
	int getCount(int bin);
	int getSpan();
	int[] getBins();
	int getAndClear(int bin);
	void transferBins(Histogram hist);
}

class CasHistogram implements Histogram {
	private final AtomicInteger[] counts;

	CasHistogram(int span){
		this.counts = new AtomicInteger[span];
		for (int i = 0; i < counts.length; i++) {
			counts[i] = new AtomicInteger();					
		}
	}

	public void increment(int bin){
		int oldValue, newValue;
		do {
			oldValue = counts[bin].get();
			newValue = oldValue + 1;
		} while (!counts[bin].compareAndSet(oldValue, newValue));
	}

	public int getCount(int bin){
		return counts[bin].get();
	}

	public int getSpan(){
		return counts.length;
	}

	public int[] getBins(){
		int[] arr = new int[counts.length];
		for (int i = 0; i < counts.length; i++) {
			arr[i] = counts[i].get();
		}
		return arr;
	}

	public int getAndClear(int bin){
		 int oldValue, newValue;
		 do {
			 oldValue = counts[bin].get();
			 newValue = 0;
		 } while (!counts[bin].compareAndSet(oldValue, newValue));
		 return oldValue;
	}

	public void transferBins(Histogram hist){
		for (int i = 0;	i < counts.length; i++){
			int oldValue, newValue;
			do{
				oldValue = counts[i].get();
				newValue = oldValue + hist.getAndClear(i);
			} while(!counts[i].compareAndSet(oldValue, newValue));
		}
	}
}