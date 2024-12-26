import bgu.spl.mics.Future;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {

    @Test
    void testResolveAndIsDone() {
        Future<String> future = new Future<>();
        assertFalse(future.isDone(), "Future should not be done initially");

        future.resolve("Result");
        assertTrue(future.isDone(), "Future should be done after resolve");
    }

    @Test
    void testGetWithoutTimeout() throws InterruptedException {
        Future<String> future = new Future<>();
        Thread resolver = new Thread(() -> {
            try {
                Thread.sleep(100); // Simulate delay before resolving
                future.resolve("Result");
            } catch (InterruptedException ignored) {
            }
        });
        resolver.start();

        assertEquals("Result", future.get(), "Future should return the resolved value");
    }

    @Test
    void testGetWithTimeout() throws InterruptedException {
        Future<String> future = new Future<>();
        String result = future.get(200, TimeUnit.MILLISECONDS);
        assertNull(result, "Future should return null if not resolved within timeout");

        future.resolve("Result");
        assertEquals("Result", future.get(200, TimeUnit.MILLISECONDS), "Future should return the resolved value if resolved before timeout");
    }
}
