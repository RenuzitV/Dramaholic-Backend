package dramaholic.movie;

import com.querydsl.core.types.dsl.BooleanExpression;
import dramaholic.actor.Actor;
import dramaholic.actor.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieScraper movieScraper;
    private final QMovie movie;

    @Autowired
    MovieService(MovieRepository movieRepository, MovieScraper movieScraper, ActorRepository actorRepository){
        this.movieRepository = movieRepository;
        this.movieScraper = movieScraper;
        this.actorRepository = actorRepository;
        this.movie = QMovie.movie;
    }

    public boolean isValid(Movie movie){
        return true;
    }

    // Add new Customer
    public String addMovie(Movie movie) {
        try {
            movieRepository.save(movie);
            return "saved";
        } catch(Exception e) {
            return "failed";
        }
    }

    @Async
    public void reloadDatabase(int g, int ko){
        List<Movie> movies = movieScraper.scrapeMovies(g, "");
        movies.addAll(movieScraper.scrapeMovies(ko, "ko"));
        movies.add(movieScraper.makeMovieFromID("99966"));

        movieScraper.uniqueMovie(movies);

        List<Actor> actorSet = new ArrayList<>();
        movies.forEach(element -> actorSet.addAll(element.getActors()));

        movieScraper.uniqueActor(actorSet);

        System.out.println(actorSet.size() + " actors");
        actorRepository.saveAll(actorSet);

        System.out.println("saved");
        System.out.println(movies.size() + " movies");

        movieRepository.saveAll(movies);
        System.out.println("done");
    }

    public Page<Movie> find(String title, Double rateGT, Double rateLTE, Long episodesGT, Long episodesLTE, String[] country, String[] genre, Pageable pagingSort) {
        BooleanExpression movieRatingBetween = movie.rating.between(rateGT, rateLTE);
        BooleanExpression movieEpisodesBetween = movie.episodes.between(episodesGT, episodesLTE);
        BooleanExpression movieTitleLike = movie.title.likeIgnoreCase("%"+title+"%");
        BooleanExpression movieCountryExp = null;
        if (country.length > 0) movieCountryExp = movie.country.in(country);
        BooleanExpression movieGenreExp = null;
        if (country.length > 0) movieGenreExp = movie.country.in(genre);

        return movieRepository.findAll(movieRatingBetween.and(movieEpisodesBetween).and(movieTitleLike).and(movieCountryExp).and(movieGenreExp), pagingSort);
    }

    public Movie findById(Long id){
        return movieRepository.findFirstByDbID(id);
    }

    public Long deleteMovieByID(Long id) {
        return movieRepository.deleteByDbID(id);
    }

    public Movie getMovie(Long dbID) {
        return movieRepository.findFirstByDbID(dbID);
    }

    public Movie addMovie(Long dbID) {
        return movieScraper.makeMovieFromID(String.valueOf(dbID));
    }

    public boolean exists(Long dbID) {
        return movieRepository.existsById(dbID);
    }
}
