package message;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Optional;

public class MessageTest {
	@Test
	public void test() throws InterruptedException, IOException {
		Port<Integer> port = new Port<>();
		Channel<Integer> a = port.newChannel(), b = port.newChannel();
		
		a.send(1);
		b.send(2);
		a.send(3);
		
		assertEquals(1, port.get().intValue());
		assertEquals(2, port.get().intValue());
		assertEquals(3, port.get().intValue());
		assertEquals(Optional.absent(), port.tryGet(100, TimeUnit.MILLISECONDS));
		
		b.send(4, 100, TimeUnit.MILLISECONDS);
		assertEquals(Optional.of(4), port.tryGet(100, TimeUnit.MILLISECONDS));
	}
}
