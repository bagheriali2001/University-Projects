package Class;

import java.time.LocalDate;

public class Post {
    private int postID;
    private String postOf;
    private String title;
    private String content;
    private LocalDate timePosted;
    private int likeCounter;

    public Post(int postID, String postOf, String title, String content, LocalDate timePosted, int likeCounter) {
        this.postID = postID;
        this.postOf = postOf;
        this.title = title;
        this.content = content;
        this.timePosted = timePosted;
        this.likeCounter=likeCounter;
    }

    public int getPostID() {
        return postID;
    }

    public String getPostOf() {
        return postOf;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getTimePosted() {
        return timePosted;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }
}
