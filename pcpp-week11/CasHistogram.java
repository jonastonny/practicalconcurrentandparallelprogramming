import java.util.concurrent.atomic.AtomicInteger;

interface Histogram {
	void increment(int bin);
	int getCount(int bin);
	int getSpan();
	int[] getBins();
	int getAndClear(int bin);
	void transferBins(Histogram hist);
}

public class CasHistogram implements Histogram {
	private AtomicInteger[] counts;

	public CasHistogram(int span) {
		counts = new AtomicInteger[span];
		for (int i = 0; i < span; i++) counts[i] = new AtomicInteger();
	}

	public void increment(int bin) {
		int oldValue, newValue;
		do {
			oldValue = counts[bin].get();
			newValue = oldValue + 1;
		} while (!counts[bin].compareAndSet(oldValue, newValue));
	}

	public int getCount(int bin) {
		return counts[bin].get();
	}

	public int getSpan() {
		return counts.length;
	}

	public int[] getBins() {
		int bins[] = new int[getSpan()];
		for(int i = 0; i < getSpan(); i++) {
			bins[i] = counts[i].get();
		}
		return bins;
	}

	public int getAndClear(int bin) {
		int b;
		do {
			b = counts[bin].get();
		}
		while(!counts[bin].compareAndSet(b, 0));
		return b;
	}

	public void transferBins(Histogram hist) {
		for(int i = 0; i < hist.getSpan(); i++) {
			int oldValue, newValue;
			do {
				oldValue = counts[i].get();
				newValue = oldValue + hist.getAndClear(i);
			} while(!counts[i].compareAndSet(oldValue, newValue));
		}
	}
}