package com.viafoura.myapplication.models;

import java.util.UUID;

public class Comment {
    private UUID uuid;
    private String comment;

    public Comment(UUID uuid, String comment) {
        this.uuid = uuid;
        this.comment = comment;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
