package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	private T result;

	// Indicator whether the result is available
	// Use volatile for visibility across threads without synchronization
	private volatile boolean isDone;

	/**
	 * This should be the only public constructor in this class.
	 */
	public Future() {
		this.result = null;
		this.isDone = false; // Explicitly initializing here
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * 	       
     */
	// Synchronization is needed to safely check isDone state and get the result
	public synchronized T get() {
		while (!isDone) {
			try {
				wait(); // Wait until the result is resolved and notify is being called
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Preserve the status of the interrupted thread
			}
		}
		return result;
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved,
	 * This method is non-blocking, it has a limited amount of time determined
	 * by {@code timeout}
	 * <p>
	 * @param timeout 	the maximal amount of time units to wait for the result.
	 * @param unit		the {@link TimeUnit} time units to wait.
	 * @return return the result of type T if it is available, if not,
	 * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
	 *         elapsed, return null.
	 */
	// Synchronization is needed to safely check isDone state and get the result
	public synchronized T get(long timeout, TimeUnit unit) {
		if (!isDone) {
			try {
				wait(unit.toMillis(timeout)); // Wait for the specified timeout
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Preserve the status of the interrupted thread
			}
		}
		return result;
	}
	
	/**
     * Resolves the result of this Future object.
     */
	// Modifies the shared state of "this" (result and isDone) and notifies waiting threads via notifyAll()
	// Synchronization is needed for changing state safely and notifying all the threads waiting for information
	public synchronized void resolve (T result) {
		if (!isDone) {
			this.result = result;
			this.isDone = true;
			notifyAll(); // Notify all threads waiting for the result
		}
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		return isDone;
	}
}
