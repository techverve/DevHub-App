package com.example.devhub.ModelClasses;

public class Model_FavoriteTextStatus {
    private String useremail,status,profileurl,currentdatetime;

    public Model_FavoriteTextStatus(){
    }

    public Model_FavoriteTextStatus(String useremail, String status, String profileurl, String currentdatetime) {
        this.useremail = useremail;
        this.status = status;
        this.profileurl = profileurl;
        this.currentdatetime = currentdatetime;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getCurrentdatetime() {
        return currentdatetime;
    }

    public void setCurrentdatetime(String currentdatetime) {
        this.currentdatetime = currentdatetime;
    }
}
