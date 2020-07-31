package com.example.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    public static final String LOG_TAG = BookLoader.class.getName();

    String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";
    String BOOK_REQUEST_MAX_RESULTS = "&maxResults=6";
    private String bookRequestContent;

    public BookLoader(Context context, String bookRequestContent) {
        super(context);
        this.bookRequestContent = bookRequestContent;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (bookRequestContent == null){
            return null;
        }
        String bookURL = BOOK_REQUEST_URL + bookRequestContent + BOOK_REQUEST_MAX_RESULTS;
        String jsonResponse = "";
        try {
            jsonResponse = QueryUtils.makeHttpRequest(bookURL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        return QueryUtils.extractBooks(jsonResponse);
        }
}