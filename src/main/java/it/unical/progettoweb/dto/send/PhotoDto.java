package it.unical.progettoweb.dto.send;

public class PhotoDto {

    private int id;
    private String url;
    private int postId;

    public PhotoDto() {}

    public PhotoDto(int id, String url, int postId) {
        this.id = id;
        this.url = url;
        this.postId = postId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
}