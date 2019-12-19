package anhem1nha.shashank.platform.fancyloginpage.Modal;

public class Comment {
    String id ;
    String name ;
    String commentContent ;
    String avatar;

    public Comment(String id, String name, String commentContent, String avatar) {
        this.id = id;
        this.name = name;
        this.commentContent = commentContent;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
