package com.codepath.bibliophile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerTouchListener onTouchListener;
    private RecyclerView mRecyclerView;
    private List<BookModel> mBook;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public TransactionRecyclerViewAdapter(Context context, List<BookModel> model) {
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
        private TextView tvBookOwner;
        private View view;

        public TextView getTvBookOwner() {
            return tvBookOwner;
        }

        public void setTvBookOwner(TextView tvBookOwner) {
            this.tvBookOwner = tvBookOwner;
        }

        public void setTvRating(RatingBar tvRating) {
            this.tvRating = tvRating;
        }

        public BookViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivBookCover = (ImageView) itemView.findViewById(R.id.ivBookCover);
            tvBookTitle = (TextView) itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = (TextView) itemView.findViewById(R.id.tvAuthorName);
            tvRating = (RatingBar) itemView.findViewById(R.id.rating);
            tvBookDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvBookOwner =(TextView) itemView.findViewById(R.id.tvBookOwner);

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

        View v1 = inflater.inflate(R.layout.transaction_recycler_view_item, parent, false);
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
            vh1.getTvPrice().setText("$" + book.getPrice().toString());
            vh1.getTvRating().setRating((float) book.getAverageRating().doubleValue());
            try {
                vh1.getTvBookOwner().setText(book.getSeller().fetchIfNeeded().getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
                    try {
                        intent.putExtra("bookOwner",book.getSeller().fetchIfNeeded().getUsername());
                        intent.putExtra("ownerEmail",book.getSeller().fetchIfNeeded().getEmail());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    getmContext().startActivity(intent);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return mBook.size();
    }

    private void setBuyer(BookModel book){

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("BookModel");
//        query.whereEqualTo("_id", book.getObjectId());

        final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BookModel");
        query.whereEqualTo("objectId",book.getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.put("isListed", false);
                object.put("buyer", user);
                object.saveEventually();

            }
        });





//
//        book.setIsListed(false);
//        book.setIsTransactionComplete(false);
//        book.setBuyer(user);
//        final String currentUser = ParseUser.getCurrentUser().getEmail();
//        user.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                Log.d("Seller: ", currentUser);
//            }
//        });

//        Log.d("Buyer:", user.toString());
//        Log.d("User:", user.getEmail());

    }



}
