package eu.goodlike.listener;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerRegistryTest {

    private interface SimpleListener {
        void onSimpleCondition();
    }

    private SimpleListener newListener() {
        return () -> {};
    }

    private ListenerRegistry<SimpleListener> listenerRegistry;

    @Before
    public void setup() {
        listenerRegistry = new ListenerRegistry<>();
    }

    @Test
    public void addedListenerIsReturned() {
        SimpleListener listener = newListener();
        listenerRegistry.addListener(listener);

        assertThat(listenerRegistry.getAllListeners())
                .contains(listener);
    }

    @Test
    public void emptyRegistryHasNoListeners() {
        assertThat(listenerRegistry.getAllListeners())
                .isEmpty();
    }

    @Test
    public void removedListenerIsNoLongerReturned() {
        SimpleListener listener = newListener();
        listenerRegistry.addListener(listener);
        listenerRegistry.removeListener(listener);

        assertThat(listenerRegistry.getAllListeners())
                .doesNotContain(listener);
    }

}