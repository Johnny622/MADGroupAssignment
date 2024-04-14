package my.edu.utar.groupassignment;

public class PaperDiscussion {
    private String discussion;
    private String user;

    public PaperDiscussion(String user, String discussion) {
        this.user = user;
        this.discussion = discussion;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }
}
