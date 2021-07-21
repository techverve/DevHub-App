package com.example.devhub.ModelClasses;

public class Model_GetTextComments {
    private String comment, commentperson, currendatetime, profilepicurl;

    public Model_GetTextComments() {

    }

    public Model_GetTextComments(String comment, String commentperson, String currendatetime, String profilepicurl) {
        this.comment = comment;
        this.commentperson = commentperson;
        this.currendatetime = currendatetime;
        this.profilepicurl =profilepicurl ;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentperson() {
        return commentperson;
    }

    public void setCommentperson(String commentperson) {
        this.commentperson = commentperson;
    }

    public String getCurrendatetime() {
        return currendatetime;
    }

    public void setCurrendatetime(String currendatetime) {
        this.currendatetime = currendatetime;
    }

    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }
}
