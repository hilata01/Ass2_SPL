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
	private static MessageBusImpl instance = null; // Singleton's single instance

	private final Map<MicroService, BlockingQueue<Message>> serviceQueues;
	private final Map<Class<? extends Message>, List<MicroService>> subscribers;
	private final Map<Event<?>, Future<?>> eventFutures;
	private final Map<Class<? extends Event<?>>, Integer> roundRobinIndices;

	// Private constructor for singleton
	private MessageBusImpl() {
		serviceQueues = new ConcurrentHashMap<>();
		subscribers = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
		roundRobinIndices = new ConcurrentHashMap<>();
	}

	// Singleton instance accessor (the one public method addition)
	// TODO: Explain why synchronized
	public static synchronized MessageBusImpl getInstance() {
		if (instance == null) {
			instance = new MessageBusImpl();
		}
		return instance;
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
	// TODO: Explain why synchronized
	public synchronized  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// In case this event type doesn't have subscribers yet, initialize it
		subscribers.putIfAbsent(type, new ArrayList<>());
		// Get the list of subscribers to this event type and add the new microservice to it
		subscribers.get(type).add(m);
	}

	@Override
	// TODO: Explain why synchronized
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// Same as subscribeEvent, this time the argument is extending broadcast
		subscribers.putIfAbsent(type, new ArrayList<>());
		subscribers.get(type).add(m);
	}

	@Override
	// TODO: Explain why synchronized
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		// Get the subscribers to this specific event. If none then it's an empty list
		List<MicroService> eventSubscribers = subscribers.getOrDefault(e.getClass(), new ArrayList<>());
		// If no subscribers then there's no event to return as result
		if (eventSubscribers.isEmpty()) {
			return null;
		}

		// Round-robin dispatch TODO: understand this
		int index = roundRobinIndices.getOrDefault(e.getClass(), 0);
		MicroService target = eventSubscribers.get(index);
		roundRobinIndices.put(e.getClass(), (index + 1) % eventSubscribers.size());

		Future<T> future = new Future<>();
		eventFutures.put(e, future);
		serviceQueues.get(target).add(e); // TODO: Decide if offer() is better than add. It returns a boolean addition result indicator
		return future;
	}

	@Override
	// TODO: Explain why synchronized
	public synchronized void sendBroadcast(Broadcast b) {
		// Get the subscribers to this specific event. If none then it's an empty list
		List<MicroService> broadcastSubscribers = subscribers.getOrDefault(b.getClass(), new ArrayList<>());
		// Add the broadcast to the queue of each subscribed microservice
		for (MicroService m : broadcastSubscribers) {
			serviceQueues.get(m).add(b); // TODO: Decide if offer() is better than add. It returns a boolean addition result indicator
		}
	}

	@Override
	// TODO: Explain why synchronized
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
	// TODO: understand this method
	public Message awaitMessage(MicroService m) throws InterruptedException {
		BlockingQueue<Message> queue = serviceQueues.get(m);
		if (queue == null) {
			throw new IllegalStateException("MicroService is not registered.");
		}
		return queue.take(); // Blocks until a message is available
	}
}
