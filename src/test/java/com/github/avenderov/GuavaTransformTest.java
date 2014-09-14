package com.github.avenderov;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class GuavaTransformTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaTransformTest.class);

    private List<Book> books;

    private InvocationCountingGetTitleFunction getTitleFunction;

    @Before
    public void setUp() {
        books = Lists
                .newArrayList(
                        Book.builder().withTitle("Java 8 Lambdas").withAuthors("Richard Warburton").build(),
                        Book.builder().withTitle(
                                "Functional Programming in Java: Harnessing the Power of Java 8 Lambda Expressions")
                                .withAuthors("Venkat Subramaniam").build(),
                        Book.builder().withTitle("Java 8 in Action: Lambdas, Streams, and Functional-Style Programming")
                                .withAuthors("Raoul-Gabriel Urma", "Mario Fusco", "Alan Mycroft").build());

        getTitleFunction = new InvocationCountingGetTitleFunction(Book.FN_GET_TITLE);
    }

    @Test
    public void testListTransformation() {
        final List<String> titles = Lists.transform(books, getTitleFunction);
        assertThat(getTitleFunction.getInvocationCount()).isZero();

        LOGGER.info(titles.toString());
        LOGGER.info(titles.toString());

        assertThat(getTitleFunction.getInvocationCount()).isEqualTo(6);
    }

    @Test
    public void testListTransformationWithWrapping() {
        final List<String> titles = Lists.newArrayList(Lists.transform(books, getTitleFunction));
        assertThat(getTitleFunction.getInvocationCount()).isEqualTo(3);

        LOGGER.info(titles.toString());
        LOGGER.info(titles.toString());

        assertThat(getTitleFunction.getInvocationCount()).isEqualTo(3);
    }

    @Test
    public void testListTransformationWithFluentIterable() {
        final List<String> titles = FluentIterable.from(books).transform(getTitleFunction).toList();
        assertThat(getTitleFunction.getInvocationCount()).isEqualTo(3);

        LOGGER.info(titles.toString());
        LOGGER.info(titles.toString());

        assertThat(getTitleFunction.getInvocationCount()).isEqualTo(3);
    }

    private static class InvocationCountingGetTitleFunction implements Function<Book, String> {

        private final Function<Book, String> getTitleFunction;

        private final AtomicInteger invocationCounter;

        public InvocationCountingGetTitleFunction(final Function<Book, String> getTitleFunction) {
            this.getTitleFunction = getTitleFunction;
            this.invocationCounter = new AtomicInteger();
        }

        @Nullable
        @Override
        public String apply(@Nullable final Book book) {
            final String result = getTitleFunction.apply(book);
            invocationCounter.incrementAndGet();
            return result;
        }

        public int getInvocationCount() {
            return invocationCounter.get();
        }

    }

}