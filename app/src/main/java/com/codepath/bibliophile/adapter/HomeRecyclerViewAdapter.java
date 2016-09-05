package com.codepath.bibliophile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.bibliophile.R;
import com.codepath.bibliophile.model.BookModel;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        private TextView tvBookOwner;
        private CircleImageView cvSeller;

        public TextView getSellerName() {
            return sellerName;
        }

        public void setSellerName(TextView sellerName) {
            this.sellerName = sellerName;
        }

        private TextView sellerName;
        //private Button buttonBuy;

        public CircleImageView getCvSeller() {
            return cvSeller;
        }

        public void setCvSeller(CircleImageView cvSeller) {
            this.cvSeller = cvSeller;
        }



//        //public Button getButtonBuy() {
//            return buttonBuy;
//        }

//        public void setButtonBuy(Button buttonBuy) {
//            this.buttonBuy = buttonBuy;
//        }

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
            tvBookOwner = (TextView) itemView.findViewById(R.id.tvBookOwner);
            cvSeller = (CircleImageView) itemView.findViewById(R.id.seller_image);
            sellerName = (TextView) itemView.findViewById(R.id.seller_name);
            //buttonBuy = (Button) itemView.findViewById(R.id.buyButton);

        }

        public View getView() {
            return view;
        }

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
        final BookViewHolder vh1 = (BookViewHolder) holder;
        final BookModel book = (BookModel) mBook.get(position);
        if (book != null) {
            vh1.getTvBookTitle().setText(book.getTitle());
            String author = "by " + book.getAuthor();
            vh1.getTvBookAuthor().setText(author);
            String price = "$" + book.getPrice().toString();
            vh1.getTvPrice().setText(price);
            vh1.getTvRating().setRating((float) book.getAverageRating().doubleValue());
            try {
                vh1.getSellerName().setText(book.getSeller().fetchIfNeeded().getUsername());
                Glide.with(mContext)
                        .load(book.getSeller().fetchIfNeeded().getString("profilePic"))
                        .into(vh1.getCvSeller());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (book.getBookCover() != null) {
                vh1.getIvBookCover().setVisibility(View.VISIBLE);
                Glide.with(mContext).load(book.getBookCover()).into(vh1.ivBookCover);
            } else {
                vh1.ivBookCover.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mBook.size();
    }

    private void setBuyer(BookModel book) {
        final ParseUser user = ParseUser.getCurrentUser();
        book.setBuyer(user);
        book.setIsListed(false);
        Log.d("BUYER", "Setting Buyer");
        book.saveEventually();
    }

}
