package dramaholic.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.Table;

@Component
@Table(name = "movie")
public class MovieDatabaseLoader implements CommandLineRunner {
    private final MovieRepository repository;
    private final MovieScraper movieScraper;

    @Autowired
    public MovieDatabaseLoader(MovieRepository repository, MovieScraper movieScraper) {
        this.repository = repository;
        this.movieScraper = movieScraper;
    }

    @Override
    public void run(String... strings) throws Exception{
//        List<Movie> movies = movieScraper.scrapeMovies(50, "ko");
//        movies.addAll(movieScraper.scrapeMovies(50, ""));
//
//
//
//        movieScraper.unique(movies);
//        this.repository.saveAll(movies);
    }
}
