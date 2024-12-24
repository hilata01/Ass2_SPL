package bgu.spl.mics;
import java.util.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only one public method (in addition to getters which can be public solely for unit testing) may be added to this class
 * All other methods and members you add the class must be private.
 */
public class MessageBusImpl implements MessageBus {

	private final Map<MicroService, BlockingQueue<Message>> serviceQueues;
	private final Map<Class<? extends Message>, List<MicroService>> subscribers;
	private final Map<Event<?>, Future<?>> eventFutures;
	// Tracks the current index of the handling microservice for each event type
	private final Map<Class<? extends Event<?>>, Integer> roundRobinIndices;

	// Private constructor to prevent instantiation of singleton
	private MessageBusImpl() {
		serviceQueues = new ConcurrentHashMap<>();
		subscribers = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
		roundRobinIndices = new ConcurrentHashMap<>();
	}

	// Static inner class to hold the singleton instance (as detailed in PS9)
	private static class SingletonHolder {
		private static final MessageBusImpl INSTANCE = new MessageBusImpl();
	}

	// Public method to provide access to the singleton instance
	public static MessageBusImpl getInstance() {
		return SingletonHolder.INSTANCE;
	}

	@Override
	// Register a microservice to the MessageBus by creating a dedicated queue for if it doesn't exist already
	// Synchronized to ensure only one thread can update serviceQueues, to prevent inconsistent or duplicate entries
	public synchronized void register(MicroService m) {
		serviceQueues.putIfAbsent(m, new LinkedBlockingQueue<>());
	}

	@Override
	// Synchronized to ensure that the un-registration process is atomic, to prevent partial removals
	public synchronized void unregister(MicroService m) {
		serviceQueues.remove(m);
		subscribers.values().forEach(list -> list.remove(m)); // TODO: ?
	}

	@Override
	// Synchronized to ensure subscribers map and its associated lists are updated consistently
	public synchronized  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// In case this event type doesn't have subscribers yet, initialize it
		subscribers.putIfAbsent(type, new ArrayList<>());
		// Get the list of subscribers to this event type and add the new microservice to it
		subscribers.get(type).add(m);
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// Same as subscribeEvent, this time the argument is extending broadcast
		subscribers.putIfAbsent(type, new ArrayList<>());
		subscribers.get(type).add(m);
	}

	@Override
	// Synchronized to ensure atomicity in selecting the target MicroService, updating indices, and queuing the event
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		// Get the list of MicroServices subscribed to the type of event e. If none then it's an empty list
		List<MicroService> eventSubscribers = subscribers.getOrDefault(e.getClass(), new ArrayList<>());
		// If no subscribers then there are no handlers available to resolve this event
		if (eventSubscribers.isEmpty()) {
			return null;
		}

		// Round-robin pattern to evenly distribute events among MicroServices subscribed to a specific event type:
		// Fetch the current index for this event type. If no index exists, start with 0
		int index = roundRobinIndices.getOrDefault(e.getClass(), 0);
		// Retrieve the subscribed microservice at the current index and then increment index
		MicroService target = eventSubscribers.get(index);
		roundRobinIndices.put((Class<? extends Event<?>>) e.getClass(), (index + 1) % eventSubscribers.size());

		// Create a new Future object for the result of the processed event and put in the map
		Future<T> future = new Future<>();
		eventFutures.put(e, future);
		// Enqueues the event for the MicroService so it can process it when it reaches this message
		serviceQueues.get(target).add(e);
		return future;
	}

	@Override
	// Synchronized to ensure subscribers map is not modified while the method is iterating on it
	public synchronized void sendBroadcast(Broadcast b) {
		// Get the subscribers to this specific event. If none then it's an empty list
		List<MicroService> broadcastSubscribers = subscribers.getOrDefault(b.getClass(), new ArrayList<>());
		// Add the broadcast to the queue of each subscribed microservice
		for (MicroService m : broadcastSubscribers) {
			serviceQueues.get(m).add(b);
		}
	}

	@Override
	// Synchronized to ensure only one thread at a time can resolve a specific future
	public synchronized <T> void complete(Event<T> e, T result) {
		// Get a future object of the wanted event type
		Future<T> future = (Future<T>) eventFutures.get(e);
		if (future != null) {
			// Resolve the future object with the given result
			future.resolve(result);
			eventFutures.remove(e);
		}
	}

	@Override
	// No need to synchronize because LinkedBlockingQueue is thread safe and take will block if needed
	// each MicroService has its own BlockingQueue, ensuring isolated and thread-safe access to its messages
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// Fetch the message queue of the given MicroService from the serviceQueues map and validate not null
		BlockingQueue<Message> queue = serviceQueues.get(m);
		if (queue == null) {
			throw new IllegalStateException("MicroService is not registered.");
		}
		return queue.take(); // blocks the calling thread until a message is available
	}
}
