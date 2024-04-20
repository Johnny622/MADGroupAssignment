package my.edu.utar.groupassignment;

public class PaperDiscussion {

    //private String user;

    private String username;
    private String discussion;

    public PaperDiscussion() {
    }

    public PaperDiscussion( String username,String discussion) {
        this.username = username;
        this.discussion = discussion;
    }


    public String getUsername() {
        return username;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }


}
