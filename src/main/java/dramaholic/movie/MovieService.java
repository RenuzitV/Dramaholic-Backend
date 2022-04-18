package dramaholic.movie;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieScraper movieScraper;
    private final dramaholic.movie.QMovie movie;

    @Autowired
    MovieService(MovieRepository movieRepository, MovieScraper movieScraper){
        this.movieRepository = movieRepository;
        this.movieScraper = movieScraper;
        this.movie = dramaholic.movie.QMovie.movie;
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
    public void reloadDatabase(){
        List<Movie> movies = movieScraper.scrapeMovies(50, "ko");
        movies.addAll(movieScraper.scrapeMovies(50, ""));

        movieScraper.unique(movies);

        movieRepository.deleteAll();
        movieRepository.saveAll(movies);
    }

    public Page<Movie> find(String title, Double rateGT, Double rateLTE, Long episodesGT, Long episodesLTE, Pageable pagingSort) {
        BooleanExpression movieRatingBetween = movie.rating.between(rateGT, rateLTE);
        BooleanExpression movieEpisodesBetween = movie.episodes.between(episodesGT, episodesLTE);
        BooleanExpression movieTitleLike = movie.title.likeIgnoreCase("%"+title+"%");

        return movieRepository.findAll(movieRatingBetween.and(movieEpisodesBetween).and(movieTitleLike), pagingSort);
    }

    public Movie findById(Long id){
        return movieRepository.findFirstByDbID(id);
    }
}
