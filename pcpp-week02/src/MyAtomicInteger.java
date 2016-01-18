import javax.annotation.concurrent.*;

@ThreadSafe
class MyAtomicInteger {
	private int atomicInteger = 0;

	public synchronized  int addAndGet(int amount) {
		atomicInteger+=amount;
		return atomicInteger;
	}
	
	public synchronized int get() {
		return atomicInteger;
	}	
}