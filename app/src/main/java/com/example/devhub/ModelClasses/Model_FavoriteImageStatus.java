package com.example.devhub.ModelClasses;

public class Model_FavoriteImageStatus {
    private String useremail,statusimageurl,status,profileurl,currentdatetime;

    public Model_FavoriteImageStatus() {
    }

    public Model_FavoriteImageStatus(String useremail, String statusimageurl, String status, String profileurl, String currentdatetime) {
        this.useremail = useremail;
        this.statusimageurl = statusimageurl;
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

    public String getStatusimageurl() {
        return statusimageurl;
    }

    public void setStatusimageurl(String statusimageurl) {
        this.statusimageurl = statusimageurl;
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
