package dramaholic.customer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import dramaholic.movie.Movie;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.servlet.annotation.MultipartConfig;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.List;

@Entity
@EnableAutoConfiguration
public class Customer implements Serializable {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    @Id
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String email;
    @OneToMany(mappedBy = "dbID", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Movie> watchLater;
    @OneToMany(mappedBy = "dbID", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Movie> history;

    public List<Movie> getWatchLater() {
        return watchLater;
    }

    public void setWatchLater(List<Movie> watchLater) {
        this.watchLater = watchLater;
    }

    public Customer(String name, LocalDate dob, String username, String password, String email, List<Movie> watchLater, List<Movie> history) {
        this.name = name;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.email = email;
        this.watchLater = watchLater;
        this.history = history;
    }

    public List<Movie> getHistory() {
        return history;
    }

    public void setHistory(List<Movie> history) {
        this.history = history;
    }

    public Customer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return username.equals(customer.username);
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

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", dob=" + dob +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", watchLater=" + watchLater +
                ", history=" + history +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
