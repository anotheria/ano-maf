package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.ActionCommand;

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
	private static final ConcurrentMap<String, ErrorHandler> instances = new ConcurrentHashMap<>();

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

	/**
	 * Allows to create instance of {@link DefaultErrorHandler} with given action command.
	 *
	 * @param actionCommand the {@link ActionCommand} which will be returned by handler
	 * @return {@link DefaultErrorHandler}
	 */
	public DefaultErrorHandler getDefaultErrorHandlerInstance(final ActionCommand actionCommand) {
		final DefaultErrorHandler handler = (DefaultErrorHandler) instances.get(DefaultErrorHandler.class.getName());
		if (handler != null) {
			return handler;
		}

		final DefaultErrorHandler newHandler = new DefaultErrorHandler(actionCommand);
		final DefaultErrorHandler existingHandler = (DefaultErrorHandler) instances.putIfAbsent(DefaultErrorHandler.class.getName(), newHandler);

		return existingHandler != null ? existingHandler : newHandler;
	}
}
