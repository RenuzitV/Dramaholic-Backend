package dramaholic.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dramaholic.customer.Customer;
import dramaholic.movie.Movie;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Comment implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    private Long id;
    private Long upvotes;
    private String message;
    @ManyToOne()
    private Customer user;
    @ManyToOne()
    @JsonBackReference("movie")
    private Movie movie;
    @ManyToMany()
//    @JsonBackReference("upvoters")
    private List<Customer> upvoters;

    public Comment(Long id, Customer user, String message, Long upvotes, Movie movie, List<Customer> upvoters) {
        this.id = id;
        this.user = user;
        this.upvotes = upvotes;
        this.message = message;
        this.movie = movie;
        this.upvoters = upvoters;
    }

    public Comment(Long id) {
        this.id = id;
    }

    @PrePersist
    public void prePersist(){
        if (upvotes == null) upvotes = 0L;
        if (message == null) message = "This movie is very good!";
    }

    public Comment() {

    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", upvotes='" + upvotes + '\'' +
                ", movie='" + movie.getDbID() + '\'' +
                ", user='" + user + '\'' +
                ", upvoters='" + upvoters + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getUser() {
        return user;
    }

    public void setUser(Customer user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Long upvotes) {
        this.upvotes = upvotes;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Customer> getUpvoters() {
        return upvoters;
    }

    public void setUpvoters(List<Customer> upvoters) {
        this.upvoters = upvoters;
    }

    public void addUpvoter(Customer customer){
        if (upvoters == null) upvoters = new ArrayList<>();
        upvoters.add(customer);
        upvotes = (long) upvoters.size();
    }

    public void removeUpvoter(Customer customer){
        if (upvoters == null) upvoters = new ArrayList<>();
        upvoters.remove(customer);
        upvotes = (long) upvoters.size();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
