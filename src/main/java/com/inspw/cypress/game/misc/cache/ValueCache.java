package com.inspw.cypress.game.misc.cache;

import java.util.Objects;
import java.util.function.Function;

public class ValueCache<T> {
    private final Function<Object[], T> function;
    private T value;
    private Object[] oldDeps;

    public ValueCache(Function<Object[], T> function, int depCount) {
        this.function = function;
        this.oldDeps = new Object[depCount];
    }

    public T getValue(Object[] deps) {
        cache(deps);
        return value;
    }

    public void cache(Object[] deps) {
        if (!shouldRecache(deps)) {
            return;
        }

        value = function.apply(deps);
        System.arraycopy(deps, 0, oldDeps, 0, deps.length);
    }

    private boolean shouldRecache(Object[] deps) {
        validateDeps(deps);
        for (int i = 0; i < oldDeps.length; i++) {
            if (!Objects.equals(oldDeps[i], deps[i])) {
                return true;
            }
        }
        return false;
    }

    private void validateDeps(Object[] deps) {
        if (deps.length != oldDeps.length) {
            throw new IllegalArgumentException("Dependency length does not match the specified length");
        }
    }
}
