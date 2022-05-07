package dramaholic.movie;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieScraper movieScraper;
    private final QMovie movie;

    @Autowired
    MovieService(MovieRepository movieRepository, MovieScraper movieScraper){
        this.movieRepository = movieRepository;
        this.movieScraper = movieScraper;
        this.movie = QMovie.movie;
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

    public boolean isValid(Movie m){
        return true;
    }

    @Async
    public void reloadDatabase(int g, int ko){
        List<Movie> movies = movieScraper.scrapeMovies(g, "");
        movies.addAll(movieScraper.scrapeMovies(ko, "ko"));
        movies.add(movieScraper.makeMovieFromID("99966"));

        movieScraper.uniqueMovie(movies);

//        List<Actor> actorSet = new ArrayList<>();
//        movies.forEach(element -> actorSet.addAll(element.getActors()));
//
//        movieScraper.uniqueActor(actorSet);
//
//        System.out.println(actorSet.size() + " actors");
//        actorRepository.saveAll(actorSet);
//        System.out.println("saved");

        System.out.println(movies.size() + " movies");
        movieRepository.saveAll(movies);
        System.out.println("done");
    }

    public Page<Movie> find(String title, Double rateGT, Double rateLTE, Long episodesGT, Long episodesLTE, String[] country, String[] genre, Pageable pagingSort) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(movie.rating.between(rateGT, rateLTE));
        booleanBuilder.and(movie.episodes.between(episodesGT, episodesLTE));
        booleanBuilder.and(
                Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", movie.title).likeIgnoreCase("%"+title+"%").or(
                Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", movie.originalTitle).likeIgnoreCase("%"+title+"%"))
        );
        if (country.length > 0) booleanBuilder.and(movie.country.in(country));
        if (genre.length > 0) booleanBuilder.and(movie.genres.any().in(genre));

        return movieRepository.findAll(booleanBuilder, pagingSort);
    }

    public Movie findById(Long id){
        return movieRepository.findFirstByDbID(id);
    }

    public Long deleteMovieByID(Long id) {
        return movieRepository.deleteByDbID(id);
    }

    public Optional<Movie> getMovie(Long dbID) {
        return movieRepository.findById(dbID);
    }

    public Movie addMovie(Long dbID) {
        return movieScraper.makeMovieFromID(String.valueOf(dbID));
    }

    public boolean exists(Long dbID) {
        return movieRepository.existsById(dbID);
    }

    // Update a Customer
    public String updateMovie(Movie s) {
        try {
            movieRepository.save(s);
            return "Updated";
        }catch(Exception e) {
            return "Failed";
        }
    }
}
