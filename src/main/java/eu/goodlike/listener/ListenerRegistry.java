package eu.goodlike.listener;

import eu.goodlike.neat.Null;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

/**
 * <pre>
 * Registers listeners so they can later be returned
 *
 * There is no specific limitation as to what is considered a listener; in general, it should be something that another
 * object needs access to in order to invoke some methods in certain circumstances
 *
 * This is the preferred method of passing listeners into an object, because it avoids cyclic runtime dependencies
 * i.e. when the listener needs a reference to the object it listens to (possibly transitively), it can be constructed
 * at a later time and then added to the registry
 * </pre>
 */
public final class ListenerRegistry<Listener> {

    /**
     * Adds given listener to this registry; if this listener is already in the list, a second copy is added
     * @throws NullPointerException if listener is null
     */
    public void addListener(Listener listener) {
        Null.check(listener).ifAny("Cannot be null: listener");
        listeners.add(listener);
    }

    /**
     * Removes given listener to this registry; if this listener is in the list multiple times, only one is removed
     * @throws NullPointerException if listener is null
     */
    public void removeListener(Listener listener) {
        Null.check(listener).ifAny("Cannot be null: listener");
        listeners.remove(listener);
    }

    /**
     * @return snapshot of all listeners at current time
     */
    public Stream<Listener> getAllListeners() {
        return listeners.stream();
    }

    // CONSTRUCTORS

    public ListenerRegistry() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    // PRIVATE

    private final List<Listener> listeners;

}
