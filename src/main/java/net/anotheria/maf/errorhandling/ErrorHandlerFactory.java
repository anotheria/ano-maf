package net.anotheria.maf.errorhandling;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link ErrorHandler} instance factory.
 *
 * @author Illya Bogatyrchuk
 */
public final class ErrorHandlerFactory {
	/**
	 * {@link ErrorHandler} instances storage.
	 */
	private static ConcurrentMap<String, ErrorHandler> instances = new ConcurrentHashMap<>();

	/**
	 * Allows to create instance of {@link ErrorHandler} subclass by given class type.
	 *
	 * @param clazz the class of {@link ErrorHandler} subclass
	 * @param <T>   the type of {@link ErrorHandler} subclass
	 * @return the instance of {@link ErrorHandler} subclass
	 */
	@SuppressWarnings("unchecked")
	public <T extends ErrorHandler> T getInstance(final Class<T> clazz) {
		final ErrorHandler handler = instances.get(clazz.getName());
		if (handler != null) {
			return (T) handler;
		}

		try {
			final T newHandler = clazz.newInstance();
			final T existingHandler = (T) instances.putIfAbsent(clazz.getName(), newHandler);

			return existingHandler != null ? existingHandler : newHandler;
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
