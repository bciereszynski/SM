package com.example.libraryapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class BookDetailsActivity extends AppCompatActivity {
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    private TextView bookTitleTextView;
    private TextView bookAuthorTextView;
    private ImageView bookCover;
    private TextView bookFirstPublishYearTextView;
    private TextView bookLanguagesTextView;
    private TextView bookTimesTextView;
    private TextView bookPersonsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        bookTitleTextView = findViewById(R.id.book_title);
        bookAuthorTextView = findViewById(R.id.book_author);
        bookFirstPublishYearTextView = findViewById(R.id.book_first_publish_year);
        bookLanguagesTextView = findViewById(R.id.book_languages);
        bookTimesTextView = findViewById(R.id.book_times);
        bookPersonsTextView = findViewById(R.id.book_persons);

        bookCover = findViewById(R.id.img_cover);

        Book book = (Book) getIntent().getSerializableExtra(MainActivity.EXTRA_BOOK_OBJ);

        try {
            bookTitleTextView.setText(book.getTitle());
        }
        catch (Exception ex){

        }

        try {
            bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
        }
        catch (Exception ex){
            bookAuthorTextView.setText(getResources().getString(R.string.data_missing));
        }
        try {
            bookFirstPublishYearTextView.setText(String.valueOf(book.getFirstPublishYear()));
        }
        catch (Exception ex){
            bookFirstPublishYearTextView.setText(getResources().getString(R.string.data_missing));
        }

        try {
            bookLanguagesTextView.setText(TextUtils.join(", ", book.getLanguages()));
        }
        catch (Exception ex){
            bookLanguagesTextView.setText(getResources().getString(R.string.data_missing));
        }

        try {
            bookTimesTextView.setText(TextUtils.join(", ", book.getTimes()));
        }
        catch (Exception ex){
            bookTimesTextView.setText(getResources().getString(R.string.data_missing));
        }

        try {
            bookPersonsTextView.setText(TextUtils.join(", ", book.getPersons()));
        }
        catch (Exception ex){
            bookPersonsTextView.setText(getResources().getString(R.string.data_missing));
        }


        if (book.getCover() != null) {
            Picasso.with(getApplicationContext())
                    .load(IMAGE_URL_BASE + book.getCover() + "-L.jpg")
                    .placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
        } else {
            bookCover.setImageResource(R.drawable.ic_baseline_book_24);
        }

    }
}