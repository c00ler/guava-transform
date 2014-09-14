package com.github.avenderov;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Book {

    private final String title;

    private final List<String> authors;

    private Book(final String title, final List<String> authors) {
        this.title = title;
        this.authors = Collections.unmodifiableList(new ArrayList<>(authors));
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public static class BookBuilder {

        private String title;

        private List<String> authors;

        private BookBuilder() {
        }

        public BookBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public BookBuilder withAuthors(final String... authors) {
            this.authors = authors == null ? null : Arrays.asList(authors);
            return this;
        }

        public Book build() {
            Preconditions.checkState(StringUtils.isNotBlank(title), "title must not be blank");
            Preconditions.checkState(CollectionUtils.isNotEmpty(authors), "authors must not be empty");

            return new Book(title, authors);
        }

    }

    public static final Function<Book, String> FN_GET_TITLE = new Function<Book, String>() {

        @Nullable
        @Override
        public String apply(@Nullable final Book book) {
            return book == null ? null : book.getTitle();
        }

    };

}
