package com.example.libraryapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_BOOK_OBJ = "pl.edu.pb.extra.book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private class BookHolder extends RecyclerView.ViewHolder{

        private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
        private ImageView coverImageView;
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView pagesTextView;
        public BookHolder(LayoutInflater inflater, ViewGroup parent ){
            super(inflater.inflate(R.layout.book_list_item, parent, false));
            titleTextView= itemView.findViewById(R.id.book_title);
            authorTextView = itemView.findViewById(R.id.book_author);
            pagesTextView = itemView.findViewById(R.id.number_of_pages);
            coverImageView = itemView.findViewById(R.id.img_cover);
        }
        public void bind(Book book){
            if(book != null && book.getTitle()!=null && book.getAuthors()!=null) {
                titleTextView.setText(book.getTitle());
                authorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                pagesTextView.setText(book.getNumberOfPages());
                View bookItem = itemView.findViewById(R.id.book_list_item);
                bookItem.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
                    intent.putExtra(EXTRA_BOOK_OBJ, book);
                    startActivity(intent);
                });
                if (book.getCover() != null) {
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.ic_baseline_book_24).into(coverImageView);
                } else {
                    coverImageView.setImageResource(R.drawable.ic_baseline_book_24);
                }
            }

        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ){
            return new BookHolder(getLayoutInflater(), parent);
        }
        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position){
            if(books!= null){
                Book book = books.get(position);
                holder.bind(book);
            }
            else{
                Log.d("MainActivity", "No books");
            }

        }
        @Override
        public int getItemCount(){

            if(books !=null) {
                return books.size();
            }
            else{
                return 0;
            }
        }

        public void setBooks(List<Book> books){
            this.books = books;
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);
        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                if ( response.body() != null) //response.code() == 200 &&
                    setupBookListView(response.body().getBookList());
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), getResources().getString(R.string.fail_message),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<Book> books) {
        RecyclerView view = findViewById(R.id.recyclerview);
        BookAdapter bookAdapter = new BookAdapter();
        bookAdapter.setBooks(books);
        view.setAdapter(bookAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }
}