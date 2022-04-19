package dramaholic.movie;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@EnableAutoConfiguration
public class Movie implements Serializable {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String originalTitle;
    @Column(nullable = false)
    private String href;
    @Id
    @GenericGenerator(name = "dbID", strategy = "dramaholic.movie.MovieSequenceGen")
    @GeneratedValue(generator = "dbID")
    @Column(nullable = false)
    private Long dbID;
    @Column(length=400)
    private String description;
    @ElementCollection
    private List<String> genres;
    private Double rating;
    private Double duration;
    private Long episodes;
    private String country;
    private LocalDate date;
    @ElementCollection
    private List<String> actor;
    @ElementCollection
    private List<String> director;
    @ElementCollection
    private List<String> tags;
    @Column(nullable = false)
    private String thumbnail;
    @OneToMany(mappedBy = "dbID", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Movie> suggestions;

    public Movie() {
    }

    @PrePersist
    void preInsert(){
        if (rating == null) rating = .0;
        if (duration == null) duration = .0;
        if (episodes == null) episodes = 0L;
        if (date == null) date = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id= " + dbID + '\'' +
                "title='" + title + '\'' +
                ", originalTitle=" + originalTitle +
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
                ", thumbnail='" + thumbnail + '\'' +
                ", suggestions=" + suggestions +
                '}';
    }

    /////////////////////////// SETTERS & GETTERS
    public Long getDbID() {
        return dbID;
    }

    public void setDbID(Long dbID) {
        this.dbID = dbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void addGenres(String genre) {
        if (this.genres == null) this.genres = new ArrayList<>();
        this.genres.add(genre);
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

    public Long getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Long episodes) {
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

    public List<String> getActor() {
        return actor;
    }

    public void setActor(List<String> actor) {
        this.actor = actor;
    }

    public List<String> getDirector() {
        return director;
    }

    public void setDirector(List<String> director) {
        this.director = director;
    }

    public void addDirector(String director) {
        if (this.director == null) this.director = new ArrayList<>();
        this.director.add(director);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Movie> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Movie> suggestions) {
        this.suggestions = suggestions;
    }

    public void addSuggestions(Movie suggestion) {
        if (this.suggestions == null) this.suggestions = new ArrayList<>();
        this.suggestions.add(suggestion);
    }
}
