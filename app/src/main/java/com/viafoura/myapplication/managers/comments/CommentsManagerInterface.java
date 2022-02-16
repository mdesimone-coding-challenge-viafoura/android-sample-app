package com.viafoura.myapplication.managers.comments;

import java.util.UUID;

public interface CommentsManagerInterface {
    interface CommentsManagerResult{
        void onSuccess();
        void onError();
    }
    interface CommentsManagerResultUUID{
        void onSuccess(UUID uuid);
        void onError();
    }

    void createCommentsContainer(String containerId, String title, CommentsManagerResultUUID callback);
    void createComment(UUID contentContainerUUID, String originTitle, String content, CommentsManagerResultUUID callback);
    void likeComment(UUID contentContainerUUID, UUID contentUUID , CommentsManagerResult callback);
    void dislikeComment(UUID contentContainerUUID, UUID contentUUID , CommentsManagerResult callback);
    void replyToComment(UUID contentContainerUUID, UUID contentUUID , String originTitle, String content, CommentsManagerResult callback);
}
