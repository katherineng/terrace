package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkForwarder {
	private NetworkForwarder() {}
	
	public static <M> void forward(
			int port,
			final Channel<M> ch,
			final Channel<Exception> ex
	) throws IOException {
		final ServerSocket server = new ServerSocket(port);
		final ExecutorService es = Executors.newCachedThreadPool();
		
		try {
			while (true) {
				final Socket s = server.accept();
				
				es.submit(new Runnable() {
					@SuppressWarnings("unchecked")
					@Override
					public void run() {
						try (ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {
							while (true) {
								ch.send((M)in.readObject());
							}
						} catch (Exception e) {
							handleException(e, ex);
						}
					}
				});
			}
		} finally {
			server.close();
		}
	}
	private static void handleException(Exception e, Channel<Exception> ch) {
		if (ch != null) {
			try {
				ch.send(e);
			} catch (InterruptedException _) {
				// TODO: Ignore?
				System.err.println("LOG: Channel write interrupted");
			} catch (IOException _) {
				// TODO: Ignore?
				System.err.println("LOG: IO error in channel write");
			}
		}
	}
}
