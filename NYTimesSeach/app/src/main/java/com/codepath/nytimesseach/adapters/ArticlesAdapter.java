package com.codepath.nytimesseach.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimesseach.R;
import com.codepath.nytimesseach.model.Document;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 9/22/17.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private final List<Document> documents;
    private final Context context;

    public ArticlesAdapter(List<Document> documents, Context context) {
        this.documents = documents;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.article_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Document document = documents.get(position);

        holder.titleTextView.setText(document.getHeadline().getMain());
        holder.snippetTextView.setText(document.getSnippet());

        if (document.getNewsDesk() == null || document.getNewsDesk().equals("")) {
            holder.newsDeskTextView.setVisibility(View.GONE);
        } else {
            holder.newsDeskTextView.setVisibility(View.VISIBLE);
            holder.newsDeskTextView.setText(document.getNewsDesk());
        }

        if (document.getThumbnailOrImage() != null) {
            holder.previewImageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Uri.parse(document.getThumbnailOrImage()))
                    .placeholder(R.drawable.ic_nocover)
                    .into(holder.previewImageView);
        } else {
            holder.previewImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.documents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.previewImaveVIew)
        protected ImageView previewImageView;

        @BindView(R.id.titleTextView)
        protected TextView titleTextView;

        @BindView(R.id.snippetTextView)
        protected TextView snippetTextView;

        @BindView(R.id.newsDeskTextView)
        protected TextView newsDeskTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
