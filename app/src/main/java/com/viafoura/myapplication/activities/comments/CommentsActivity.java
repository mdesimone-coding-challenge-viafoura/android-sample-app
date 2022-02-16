package com.viafoura.myapplication.activities.comments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.viafoura.myapplication.R;
import com.viafoura.myapplication.activities.base.BaseActivity;
import com.viafoura.myapplication.activities.comments.adapter.CommentsActivityIntentKeys;
import com.viafoura.myapplication.activities.comments.adapter.CommentsAdapter;
import com.viafoura.myapplication.activities.main.MainViewModel;
import com.viafoura.myapplication.application.SampleApplication;

import java.util.ArrayList;
import java.util.UUID;

public class CommentsActivity extends BaseActivity implements CommentsAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;

    private CommentsViewModel commentsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Comments");

        commentsViewModel = new CommentsViewModel(((SampleApplication) getApplicationContext()).dependencyManager.provideCommentsManager(), getIntent().getStringExtra(CommentsActivityIntentKeys.ARTICLE_ID), getIntent().getStringExtra(CommentsActivityIntentKeys.ARTICLE_TITLE));

        mRecyclerView = findViewById(R.id.comments_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CommentsAdapter adapter = new CommentsAdapter(this, commentsViewModel.commentList);
        adapter.setClickListener(this);

        mRecyclerView.setAdapter(adapter);

        findViewById(R.id.comments_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewCommentAlert();
            }
        });
    }

    private void showNewCommentAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layoutHolder = new LinearLayout(this);
        layoutHolder.setOrientation(LinearLayout.VERTICAL);

        final EditText contentEditText = new EditText(this);
        contentEditText.setHint("Comment");

        layoutHolder.addView(contentEditText);

        alert.setView(layoutHolder);

        alert.setIcon(null);
        alert.setTitle("New comment");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String content = contentEditText.getText().toString().trim();
                commentsViewModel.createComment(content, new CommentsViewModel.CreateCommentInterface() {
                    @Override
                    public void onSuccess() {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        Toast.makeText(CommentsActivity.this, "Comment submitted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(CommentsActivity.this, "An error has ocurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        alert.create();
        alert.show();
    }

    private void showReplyAlert(UUID contentId){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layoutHolder = new LinearLayout(this);
        layoutHolder.setOrientation(LinearLayout.VERTICAL);

        final EditText emailEditText = new EditText(this);
        emailEditText.setHint("Reply");

        layoutHolder.addView(emailEditText);

        alert.setView(layoutHolder);

        alert.setIcon(null);
        alert.setTitle("Leave a reply");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String content = emailEditText.getText().toString().trim();
                commentsViewModel.replyToComment(contentId, content, new CommentsViewModel.ReplyCommentInterface() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(CommentsActivity.this, "Replied sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(CommentsActivity.this, "An error has ocurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        alert.create();
        alert.show();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onCommentLiked(UUID commentId) {
        commentsViewModel.likeComment(commentId, new CommentsViewModel.LikeCommentInterface() {
            @Override
            public void onSuccess() {
                Toast.makeText(CommentsActivity.this, "Liked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(CommentsActivity.this, "An error has ocurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCommentDisliked(UUID commentId) {
        commentsViewModel.dislikeComment(commentId, new CommentsViewModel.DislikeCommentInterface() {
            @Override
            public void onSuccess() {
                Toast.makeText(CommentsActivity.this, "Disliked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(CommentsActivity.this, "An error has ocurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCommentReply(UUID commentId) {
        showReplyAlert(commentId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}