package dramaholic.customer;

import dramaholic.movie.Movie;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Deque;
import java.util.Objects;
import java.util.Set;

@Entity
@EnableAutoConfiguration
public class Customer {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    private String name;
    private Long age;
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

    public Customer(String name, Long age, String username, String password, String email, Set<Movie> watchLater, Set<Movie> history) {
        this.name = name;
        this.age = age;
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

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", age=" + age +
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

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
