package dramaholic.movie;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@EnableAutoConfiguration
public class Movie {
    private @Id
    @GeneratedValue
    @Column(nullable = false)
    Long id;
    @Column(nullable = false)
    private Long title;
    @Column(nullable = false)
    private String href;
    private String description;
    private String genres;
    private Double rating;
    private Double duration;
    private Double episodes;
    private String country;
    private LocalDate date;
    private String actor;
    private String director;
    private String tags;
    @Lob
    private byte[] thumbnail;
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private List<Movie> suggestions;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Movie(Long title, String href, String description, String genres, Double rating, Double duration, Double episodes, String country, LocalDate date, String actor, String director, String tags, byte[] thumbnail, List<Movie> suggestions) {
        this.title = title;
        this.href = href;
        this.description = description;
        this.genres = genres;
        this.rating = rating;
        this.duration = duration;
        this.episodes = episodes;
        this.country = country;
        this.date = date;
        this.actor = actor;
        this.director = director;
        this.tags = tags;
        this.thumbnail = thumbnail;
        this.suggestions = suggestions;
    }

    public Long getTitle() {
        return title;
    }

    public void setTitle(Long title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Double episodes) {
        this.episodes = episodes;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Movie> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Movie> suggestions) {
        this.suggestions = suggestions;
    }

    public Movie() {
    }


    @Override
    public String toString() {
        return "Movie{" +
                "title=" + title +
                ", href='" + href + '\'' +
                ", description='" + description + '\'' +
                ", genres='" + genres + '\'' +
                ", rating=" + rating +
                ", duration=" + duration +
                ", episodes=" + episodes +
                ", country='" + country + '\'' +
                ", date=" + date +
                ", actor='" + actor + '\'' +
                ", director='" + director + '\'' +
                ", tags='" + tags + '\'' +
                ", thumbnail=" + Arrays.toString(thumbnail) +
                ", suggestions=" + suggestions +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
