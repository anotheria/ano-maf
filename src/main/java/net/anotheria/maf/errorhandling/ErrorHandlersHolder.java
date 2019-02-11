package net.anotheria.maf.errorhandling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link ErrorHandler} holder.
 *
 * @author Illya Bogatyrchuk
 */
public class ErrorHandlersHolder {
	/**
	 * The errors to the list of error handlers mapping.
	 */
	private ConcurrentMap<Class<? extends Throwable>, List<Class<? extends ErrorHandler>>> handlers = new ConcurrentHashMap<>();

	/**
	 * Allows to specify the error handler of the given error.
	 *
	 * @param error   the class of error
	 * @param handler the class of error handler
	 */
	public synchronized void addHandler(final Class<? extends Throwable> error, final Class<? extends ErrorHandler> handler) {
		List<Class<? extends ErrorHandler>> handlers = this.handlers.get(error);
		if (handlers == null) {
			handlers = new ArrayList<>();
		}

		handlers.add(handler);
		this.handlers.put(error, handlers);
	}

	/**
	 * Returns the list of error handler types which can handle the given error type.
	 *
	 * @param errorClazz the class of error
	 * @return the list of error handler types
	 */
	public List<Class<? extends ErrorHandler>> getHandlers(final Class<? extends Throwable> errorClazz) {
		final List<Class<? extends ErrorHandler>> handlers = this.handlers.get(errorClazz);
		if (handlers == null) {
			return Collections.emptyList();
		}

		return new ArrayList<>(handlers);
	}
}
