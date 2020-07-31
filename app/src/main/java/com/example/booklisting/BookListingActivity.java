package com.example.booklisting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookListingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    /*String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=6";*/

    String book_url;
    TextView emptyStateTextView;
    //ListView bookListView;
    ArrayList<Book> books;
    BookListingAdapter bookListingAdapter;
    int requestID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        books = new ArrayList<Book>();
        bookListingAdapter = new BookListingAdapter(this,books);

        ListView bookListView = (ListView) findViewById(R.id.book_list);
        emptyStateTextView = findViewById(R.id.empty_list_item);
        bookListView.setEmptyView(emptyStateTextView);
        bookListView.setAdapter(bookListingAdapter);

        Button searchButton = findViewById(R.id.search_button);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BookListingActivity.this, "Button is pressed", Toast.LENGTH_LONG).show();

                TextView searchTextView = findViewById(R.id.search_string);
                book_url = searchTextView.getText().toString();

                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                if (networkInfo != null && networkInfo.isConnected()){
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(requestID, null, BookListingActivity.this);
                    requestID += 1;
                } else {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    emptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });
    }

    public void updateUI(List<Book> earthquakeList){
        bookListingAdapter.clear();
        if (earthquakeList != null && !earthquakeList.isEmpty()) {
            bookListingAdapter.addAll(earthquakeList);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, book_url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        emptyStateTextView.setText(R.string.no_book);
        updateUI(books);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookListingAdapter.clear();
    }
}