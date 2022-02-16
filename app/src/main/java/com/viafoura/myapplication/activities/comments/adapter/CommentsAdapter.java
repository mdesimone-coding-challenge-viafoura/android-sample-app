package com.viafoura.myapplication.activities.comments.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.viafoura.myapplication.R;
import com.viafoura.myapplication.models.Comment;

import java.util.List;
import java.util.UUID;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public CommentsAdapter(Context context, List<Comment> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mData.get(position);

        holder.contentText.setText(comment.getComment());

        holder.replyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCommentReply(comment.getUuid());
            }
        });

        holder.likeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCommentLiked(comment.getUuid());
            }
        });

        holder.disLikeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCommentDisliked(comment.getUuid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView likeText;
        TextView disLikeText;
        TextView contentText;
        TextView replyText;

        ViewHolder(View itemView) {
            super(itemView);
            likeText = itemView.findViewById(R.id.row_comment_like);
            contentText = itemView.findViewById(R.id.row_comment_content);
            replyText = itemView.findViewById(R.id.row_comment_reply);
            disLikeText = itemView.findViewById(R.id.row_comment_dislike);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Comment getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onCommentLiked(UUID commentId);
        void onCommentDisliked(UUID commentId);
        void onCommentReply(UUID commentId);
    }
}
