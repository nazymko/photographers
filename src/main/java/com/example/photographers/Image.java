package com.example.photographers;

/**
 * User: patronus
 */
public class Image {
    String smallImageUrl;
    String normalImagePage;
    String imageName = "*.*";
    String rate = "0";
    String author = "*.*";
    String authorPage;

    public String getAuthorPage() {
        return authorPage;
    }

    public void setAuthorPage(String authorPage) {
        this.authorPage = authorPage;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getNormalImagePage() {
        return normalImagePage;
    }

    public void setNormalImagePage(String normalImagePage) {
        this.normalImagePage = normalImagePage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Image{" +
                "smallImageUrl='" + smallImageUrl + '\'' +
                ", normalImagePage='" + normalImagePage + '\'' +
                ", imageName='" + imageName + '\'' +
                ", rate='" + rate + '\'' +
                ", author='" + author + '\'' +
                ", authorPage='" + authorPage + '\'' +
                '}';
    }
}
