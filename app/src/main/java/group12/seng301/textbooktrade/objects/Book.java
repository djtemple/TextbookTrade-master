package group12.seng301.textbooktrade.objects;

import group12.seng301.textbooktrade.RegisterActivity;


public class Book {

    private String name, author, imageURL;
    private RegisterActivity.Major topic;
    private User user;
    private int usefullness, condition;

    public Book(String name, RegisterActivity.Major topic) {
        this.name = name;
        this.topic = topic;
    }

    public Book(String name, RegisterActivity.Major topic, String author) {
        this.name = name;
        this.topic = topic;
        this.author = author;
    }

    public Book(Book book) {
        this.name = book.getName();
        this.author = book.getAuthor();
        this.topic = book.getTopic();
        this.user = new User(user);
    }

    public boolean hasAuthor() {
        return author.isEmpty() ? false : true;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return this.name;
    }

    public RegisterActivity.Major getTopic() {
        return topic;
    }

    public User getUser() {
        return new User(this.user);
    }

    public void setUser(User user) {
        this.user = new User(user);
    }

    public int getUsefullness() {
        return usefullness;
    }

    public int getCondition() {
        return condition;
    }

    public void setUsefullness(int usefullness) throws RatingOutOfRangeException {
        if (condition < 0 || condition > 5) throw new RatingOutOfRangeException();
        this.usefullness = usefullness;
    }

    public void setCondition(int condition) throws RatingOutOfRangeException {
        if (condition < 0 || condition > 5) throw new RatingOutOfRangeException();
        this.condition = condition;
    }

    public class RatingOutOfRangeException extends Exception {

        public RatingOutOfRangeException() {
            super("Rating must be [0, 5]!");
        }

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
