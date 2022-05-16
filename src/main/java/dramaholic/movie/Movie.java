package dramaholic.movie;

import dramaholic.actor.Actor;
import dramaholic.comment.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private boolean adult;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    private List<Actor> actors;
    @ElementCollection
    private List<String> director;
    @ElementCollection
    private List<String> tags;
    @Column
    private String thumbnail;
    @Column()
    private String thumbnail_portrait;
    @Column()
    private String thumbnail_landscape;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Movie> suggestions;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;


    public Movie() {
    }

    @PrePersist
    void preInsert(){
        if (rating == null) rating = .0;
        if (duration == null) duration = .0;
        if (episodes == null) episodes = 0L;
        if (date == null) date = LocalDate.now();
        if (title == null) title = "";
        if (originalTitle == null) originalTitle = "";
        if (href == null) href = "";
        if (thumbnail == null) thumbnail = "";
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + dbID +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", href='" + href + '\'' +
                ", description='" + description + '\'' +
                ", genres='" + genres + '\'' +
                ", rating=" + rating +
                ", duration=" + duration +
                ", episodes=" + episodes +
                ", country='" + country + '\'' +
                ", date=" + date +
                ", actors='" + actors + '\'' +
                ", director='" + director + '\'' +
                ", tags='" + tags + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", thumbnail_landscape='" + thumbnail_landscape + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return dbID.equals(movie.dbID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbID);
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

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
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

    public void addSuggestion(Movie suggestion) {
        if (this.suggestions == null) this.suggestions = new ArrayList<>();
        this.suggestions.add(suggestion);
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getThumbnail_portrait() {
        return thumbnail_portrait;
    }

    public void setThumbnail_portrait(String thumbnail_portrait) {
        this.thumbnail_portrait = thumbnail_portrait;
    }

    public String getThumbnail_landscape() {
        return thumbnail_landscape;
    }

    public void setThumbnail_landscape(String thumbnail_landscape) {
        this.thumbnail_landscape = thumbnail_landscape;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment){
        if (this.comments == null) this.comments = new ArrayList<>();
        this.comments.add(comment);
    }

    public void addActor(Actor actor){
        if (this.actors == null) this.actors = new ArrayList<>();
        this.actors.add(actor);
    }
}
