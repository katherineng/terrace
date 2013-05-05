package terrace.message;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface Channel<M> extends Closeable {
	public void send(M msg) throws IOException, InterruptedException;
	public boolean send(M msg, long timeout, TimeUnit unit) throws IOException;
}
