package com.example.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import java.util.ArrayList;

public class BookListingAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookListingAdapter.class.getSimpleName();

    public BookListingAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        String author = currentBook.getAuthor();
        authorTextView.setText(author);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        String title = currentBook.getTitle();
        titleTextView.setText(title);

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.description);
        String description = currentBook.getDescription();
        descriptionTextView.setText(description);

        return listItemView;
    }
}
