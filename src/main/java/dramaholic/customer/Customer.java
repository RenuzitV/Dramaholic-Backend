package dramaholic.customer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dramaholic.movie.Movie;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@EnableAutoConfiguration
public class Customer implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String email;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Movie> watchLater;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Movie> history;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @PrePersist
    void preInsert(){
        if (watchLater == null) watchLater = new ArrayList<>();
        if (history == null) history = new ArrayList<>();
        if (email == null) email = "";
    }

    public List<Movie> getWatchLater() {
        return watchLater;
    }

    public void setWatchLater(List<Movie> watchLater) {
        this.watchLater = watchLater;
    }

    public Customer(Long id, String name, LocalDate dob, String username, String password, String email, List<Movie> watchLater, List<Movie> history) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.email = email;
        this.watchLater = watchLater;
        this.history = history;
    }

    public Customer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id) && name.equals(customer.name) && dob.equals(customer.dob) && username.equals(customer.username) && password.equals(customer.password) && Objects.equals(email, customer.email) && Objects.equals(watchLater, customer.watchLater) && Objects.equals(history, customer.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dob, username, password, email, watchLater, history);
    }

    public boolean checkCredentials(Customer customer){
        return username.equals(customer.username) && password.equals(customer.password);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", dob=" + dob +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", watchLater='" + watchLater + '\'' +
                ", history='" + history + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Movie> getHistory() {
        return history;
    }

    public void setHistory(List<Movie> history) {
        this.history = history;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addWatchlater(Movie movie){
        watchLater.add(movie);
    }

    public void addHistory(Movie movie){
        history.add(movie);
        if (history.size() > 10) history.remove(0);
    }

    public void removeWatchlater(Movie movie) {
        watchLater.remove(movie);
    }

    public void removeHistory(Movie movie) {
        watchLater.remove(movie);
    }
}
