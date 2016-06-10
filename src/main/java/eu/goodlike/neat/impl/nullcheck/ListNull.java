package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

import java.util.List;

public final class ListNull extends Null {

    @Override
    public boolean containsNull() {
        return list.contains(null);
    }

    @Override
    protected int indexOfNull() {
        return list.indexOf(null);
    }

    @Override
    protected String contentToString() {
        return String.valueOf(list);
    }

    // CONSTRUCTORS

    public ListNull(List<?> list) {
        this.list = list;
    }

    // PRIVATE

    private final List<?> list;

}
