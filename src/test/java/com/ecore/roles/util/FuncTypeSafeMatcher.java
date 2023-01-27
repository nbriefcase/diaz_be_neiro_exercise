package com.ecore.roles.util;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FuncTypeSafeMatcher<T> extends TypeSafeMatcher<T> {
    private final Predicate<T> matchesSafely;
    private final BiConsumer<T, Description> describeMismatchSafely;
    private final Consumer<Description> describeTo;

    public FuncTypeSafeMatcher(
            Predicate<T> matchesSafely,
            Consumer<Description> describeTo,
            BiConsumer<T, Description> describeMismatchSafely) {
        this.matchesSafely = matchesSafely;
        this.describeTo = describeTo;
        this.describeMismatchSafely = describeMismatchSafely;
    }

    @Override
    protected boolean matchesSafely(T item) {
        return matchesSafely.test(item);
    }

    @Override
    public void describeTo(Description description) {
        describeTo.accept(description);
    }

    @Override
    protected void describeMismatchSafely(T item, Description description) {
        describeMismatchSafely.accept(item, description);
    }
}
