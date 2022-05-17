package dramaholic.movie;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import dramaholic.actor.Actor;
import dramaholic.actor.ActorRepository;
import dramaholic.comment.CommentRepository;
import dramaholic.customer.Customer;
import dramaholic.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieScraper movieScraper;
    private final ActorRepository actorRepository;
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final QMovie movie;
    private final List<String> genres;

    @Autowired
    MovieService(MovieRepository movieRepository, MovieScraper movieScraper, ActorRepository actorRepository, CustomerRepository customerRepository, CommentRepository commentRepository){
        this.movieRepository = movieRepository;
        this.movieScraper = movieScraper;
        this.actorRepository = actorRepository;
        this.movie = QMovie.movie;
        this.customerRepository = customerRepository;
        this.commentRepository = commentRepository;
        genres = new ArrayList<>(Arrays.asList(
                "Action & Adventure",
                "Animation",
                "Comedy",
                "Crime",
                "Documentary",
                "Drama",
                "Family",
                "Kids",
                "Mystery",
                "News",
                "Reality",
                "Sci-Fi & Fantasy",
                "Soap",
                "Talk",
                "War & Politics",
                "Western"
        ));
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

//    @Async
    public void reloadDatabase(HashMap<String, String> countries){
        List<Movie> movies = new ArrayList<>();
        countries.forEach((country, count) -> {
            if (country.equals("g")) country = null;
            movies.addAll(movieScraper.scrapeMovies(Integer.parseInt(count), country, null));
        });
        movies.add(movieScraper.makeMovieFromID("99966"));
        for (String genre : genres){
            movies.addAll(movieScraper.scrapeMovies(20, null, genre));
        }

        movieScraper.uniqueMovie(movies);

        List<Actor> actorSet = new ArrayList<>();
        movies.forEach(element -> actorSet.addAll(element.getActors()));

        movieScraper.uniqueActor(actorSet);

        List<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {
            customer.setWatchLater(List.of());
            customer.setHistory(List.of());
        }

//        customerRepository.saveAll(customers);

        commentRepository.deleteAll();
        actorRepository.deleteAll();
        movieRepository.deleteAll();

        System.out.println(actorSet.size() + " actors");
        actorRepository.saveAll(actorSet);
        System.out.println("saved");

        System.out.println(movies.size() + " movies");
        movieRepository.saveAll(movies);
        System.out.println("done");
    }

    public Page<Movie> find(String title, Double rateGT, Double rateLTE, Long episodesGT, Long episodesLTE, LocalDate dateGT, LocalDate dateLTE, String[] country, String[] genre, Pageable pagingSort) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(movie.rating.between(rateGT, rateLTE));
        booleanBuilder.and(movie.episodes.between(episodesGT, episodesLTE));
        booleanBuilder.and(
                Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", movie.title).likeIgnoreCase("%"+title+"%").or(
                Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", movie.originalTitle).likeIgnoreCase("%"+title+"%"))
        );
        if (country.length > 0) booleanBuilder.and(movie.country.in(country));
        if (genre.length > 0) booleanBuilder.and(movie.genres.any().in(genre));
        if (dateGT != null) booleanBuilder.and(movie.date.goe(dateGT));
        if (dateLTE != null) booleanBuilder.and(movie.date.loe(dateLTE));

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

    // Update a Customer
    public String updateMovie(Long id, Movie s) {
        try {
            s.setDbID(id);
            movieRepository.save(s);
            return "Updated";
        }catch(Exception e) {
            return "Failed";
        }
    }
}
