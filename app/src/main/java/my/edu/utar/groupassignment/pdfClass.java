package my.edu.utar.groupassignment;

public class pdfClass {

    public String comment,url;

    public pdfClass() {
    }

    public pdfClass(String comment, String url) {
        this.comment = comment;
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
