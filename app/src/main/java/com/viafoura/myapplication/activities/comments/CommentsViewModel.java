package com.viafoura.myapplication.activities.comments;

import com.viafoura.myapplication.activities.base.BaseViewModel;
import com.viafoura.myapplication.managers.comments.CommentsManager;
import com.viafoura.myapplication.managers.comments.CommentsManagerInterface;
import com.viafoura.myapplication.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommentsViewModel extends BaseViewModel {
    private CommentsManager commentsManager;

    private UUID commentContainerUUID;

    public List<Comment> commentList = new ArrayList<>();

    private String articleTitle = "";
    private String articleId = "";

    public CommentsViewModel(CommentsManager commentsManager, String articleId, String articleTitle){
        this.commentsManager = commentsManager;
        this.articleId = articleId;
        this.articleTitle = articleTitle;

        createCommentContainer();
    }

    public void createComment(String content, CreateCommentInterface createCommentInterface){
        if(content.trim().isEmpty()){
            createCommentInterface.onError();
            return;
        }
        if(commentContainerUUID == null){
            createCommentInterface.onError();
            return;
        }

        commentsManager.createComment(commentContainerUUID, articleTitle, content, new CommentsManagerInterface.CommentsManagerResultUUID() {
            @Override
            public void onSuccess(UUID uuid) {
                commentList.add(new Comment(uuid, content));
                createCommentInterface.onSuccess();
            }

            @Override
            public void onError() {
                createCommentInterface.onError();
            }
        });
    }

    public void likeComment(UUID contentId, LikeCommentInterface likeCommentInterface){
        commentsManager.likeComment(commentContainerUUID, contentId, new CommentsManagerInterface.CommentsManagerResult() {
            @Override
            public void onSuccess() {
                likeCommentInterface.onSuccess();
            }

            @Override
            public void onError() {
                likeCommentInterface.onError();
            }
        });
    }

    public void dislikeComment(UUID contentId, DislikeCommentInterface dislikeCommentInterface){
        commentsManager.dislikeComment(commentContainerUUID, contentId, new CommentsManagerInterface.CommentsManagerResult() {
            @Override
            public void onSuccess() {
                dislikeCommentInterface.onSuccess();
            }

            @Override
            public void onError() {
                dislikeCommentInterface.onError();
            }
        });
    }

    public void createCommentContainer(){
        commentsManager.createCommentsContainer(articleId, articleTitle, new CommentsManagerInterface.CommentsManagerResultUUID() {
            @Override
            public void onSuccess(UUID uuid) {
                commentContainerUUID = uuid;
            }

            @Override
            public void onError() {

            }
        });
    }

    public void replyToComment(UUID contentId, String reply, ReplyCommentInterface replyCommentInterface){
        if(reply.trim().isEmpty()){
            replyCommentInterface.onError();
            return;
        }
        commentsManager.replyToComment(commentContainerUUID, contentId, articleTitle, reply, new CommentsManagerInterface.CommentsManagerResult() {
            @Override
            public void onSuccess() {
                replyCommentInterface.onSuccess();
            }

            @Override
            public void onError() {
                replyCommentInterface.onError();
            }
        });
    }

    public interface CreateCommentInterface {
        void onSuccess();
        void onError();
    }

    public interface LikeCommentInterface {
        void onSuccess();
        void onError();
    }

    public interface DislikeCommentInterface {
        void onSuccess();
        void onError();
    }

    public interface ReplyCommentInterface {
        void onSuccess();
        void onError();
    }
}
