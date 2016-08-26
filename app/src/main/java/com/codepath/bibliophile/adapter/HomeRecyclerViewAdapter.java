package com.codepath.bibliophile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.codepath.bibliophile.activity.DetailsActivity;
import com.codepath.bibliophile.model.BookModel;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BookModel> mBook;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public HomeRecyclerViewAdapter(Context context, List<BookModel> model) {
        this.mBook = model;
        this.mContext = context;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBookCover;
        private TextView tvBookTitle;
        private TextView tvBookAuthor;
        private RatingBar tvRating;
        private TextView tvBookDescription;
        private TextView tvPrice;
        private View view;

        public BookViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivBookCover = (ImageView) itemView.findViewById(R.id.ivBookCover);
            tvBookTitle = (TextView) itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = (TextView) itemView.findViewById(R.id.tvAuthorName);
            tvRating = (RatingBar) itemView.findViewById(R.id.rating);
            tvBookDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        }
        public View getView() { return view; }
        public ImageView getIvBookCover() {
            return ivBookCover;
        }

        public void setIvBookCover(ImageView ivBookCover) {
            this.ivBookCover = ivBookCover;
        }

        public TextView getTvBookTitle() {
            return tvBookTitle;
        }

        public void setTvBookTitle(TextView tvBookTitle) {
            this.tvBookTitle = tvBookTitle;
        }

        public TextView getTvBookAuthor() {
            return tvBookAuthor;
        }

        public void setTvBookAuthor(TextView tvBookAuthor) {
            this.tvBookAuthor = tvBookAuthor;
        }

        public RatingBar getTvRating() {
            return tvRating;
        }

//        public void setTvRating(String tvRating) {
//            this.tvRating = tvRating;
//        }

        public TextView getTvBookDescription() {
            return tvBookDescription;
        }

        public void setTvBookDescription(TextView tvBookDescription) {
            this.tvBookDescription = tvBookDescription;
        }

        public TextView getTvPrice() {
            return tvPrice;
        }

        public void setTvPrice(TextView tvPrice) {
            this.tvPrice = tvPrice;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v1 = inflater.inflate(R.layout.home_recycler_view_item, parent, false);
        viewHolder = new BookViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookViewHolder vh1 = (BookViewHolder) holder;
        final BookModel book = (BookModel) mBook.get(position);
        if (book != null) {
            vh1.getTvBookTitle().setText(book.getTitle());
            vh1.getTvBookAuthor().setText(book.getAuthor());
            vh1.getTvBookDescription().setText(book.getDescription());
            vh1.getTvPrice().setText(book.getPrice().toString());
            vh1.getTvRating().setRating((float) book.getAverageRating().doubleValue());
            Log.d(book.toString(), "onBindViewHolder: ");

            if (book.getBookCover() != null) {
                vh1.getIvBookCover().setVisibility(View.VISIBLE);
                Glide.with(mContext).load(book.getBookCover()).into(vh1.ivBookCover);
            } else {
                vh1.ivBookCover.setVisibility(View.GONE);
            }

            vh1.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getmContext(),"details",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getmContext(), DetailsActivity.class);
                    intent.putExtra("title", book.getTitle());
                    intent.putExtra("author",book.getAuthor());
                    intent.putExtra("description",book.getDescription());
                    intent.putExtra("price",book.getPrice().toString());
                    intent.putExtra("cover",book.getBookCover());
                    intent.putExtra("isbn",String.valueOf(book.getISBN()));
                    intent.putExtra("condition",book.getCondition());
                    getmContext().startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mBook.size();
    }


}
