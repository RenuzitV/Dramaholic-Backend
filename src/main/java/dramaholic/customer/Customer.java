package dramaholic.customer;

import dramaholic.movie.Movie;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Deque;
import java.util.Objects;
import java.util.Set;

@Entity
@EnableAutoConfiguration
public class Customer {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate dob;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String email;
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<Movie> watchLater;
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<Movie> history;

    public Set<Movie> getWatchLater() {
        return watchLater;
    }

    public void setWatchLater(Set<Movie> watchLater) {
        this.watchLater = watchLater;
    }

    public Customer(String name, LocalDate dob, String username, String password, String email, Set<Movie> watchLater, Set<Movie> history) {
        this.name = name;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.email = email;
        this.watchLater = watchLater;
        this.history = history;
    }

    public Set<Movie> getHistory() {
        return history;
    }

    public void setHistory(Set<Movie> history) {
        this.history = history;
    }

    public Customer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id) && username.equals(customer.username);
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
        return Objects.hash(id, username);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
