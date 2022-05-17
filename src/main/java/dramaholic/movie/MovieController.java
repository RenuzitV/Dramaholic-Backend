package dramaholic.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    @Autowired
    MovieController(MovieService movieService, MovieRepository movieRepository){
        this.movieRepository = movieRepository;
        this.movieService = movieService;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) return Sort.Direction.ASC;
        return Sort.Direction.DESC;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        if (movieService.isValid(movie)) {
            return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @PutMapping(value ="/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> updateMovie(@PathVariable Long id, @RequestBody Movie movie){
        boolean ok = movieService.exists(id);
        if (!ok) return new ResponseEntity<>("movie does not exist", HttpStatus.NOT_FOUND);
//        Movie movie = optionalMovie.get();
//        if (body.get("date") != null) movie.setDate(LocalDate.parse(body.get("date"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//        if (body.get("thumbnail") != null) movie.setThumbnail(body.get("thumbnail"));
//        if (body.get("country") != null) movie.setCountry(body.get("country"));
//        if (body.get("originalTitle") != null) movie.setOriginalTitle(body.get("originalTitle"));
//        if (body.get("rating") != null) movie.setRating(Double.valueOf(body.get("rating")));
//        if (body.get("episodes") != null) movie.setEpisodes(Long.valueOf(body.get("episodes")));
//        if (body.get("thumbnail_landscape") != null) movie.setThumbnail_landscape(body.get("thumbnail_landscape"));
//        if (body.get("href") != null) movie.setHref(body.get("href"));
//        if (body.get("href") != null) movie.set(body.get("dbID"));
        return new ResponseEntity<>(movieService.updateMovie(id, movie), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Movie getMovie(@PathVariable("id") long id){
        return movieService.findById(id);
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Page<Movie>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "rating,desc") String[] sort){
        try {
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < sort.length; i += 2){
                if (i + 1 < sort.length) {
                    orders.add(new Order(getSortDirection(sort[i+1]), sort[i]));
                }
                else {
                    orders.add(new Order(getSortDirection("desc"), sort[i]));
                }
            }

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            Page<Movie> moviePage = movieRepository.findAll(pagingSort);

            HttpHeaders responseHeaders = new HttpHeaders();
            List<MediaType> medias = new ArrayList<>();
            medias.add(MediaType.ALL);
            responseHeaders.setAccept(medias);

            return new ResponseEntity<>(moviePage, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/search")
    @ResponseBody
    public ResponseEntity<Page<Movie>> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "rating,desc") String[] sort,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") Double rateGT,
            @RequestParam(defaultValue = "10") Double rateLTE,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateGT,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateLTE,
            @RequestParam(defaultValue = "0") Long episodesGT,
            @RequestParam(defaultValue = "10000") Long episodesLTE,
            @RequestParam(defaultValue = "") String[] country,
            @RequestParam(defaultValue = "") String[] genre){
        try {
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < sort.length; i += 2){
                if (i + 1 < sort.length) {
                    orders.add(new Order(getSortDirection(sort[i+1]), sort[i]));
                }
                else {
                    orders.add(new Order(getSortDirection("desc"), sort[i]));
                }
            }
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Movie> moviePage = movieService.find(title, rateGT, rateLTE, episodesGT, episodesLTE, dateGT, dateLTE, country, genre, pagingSort);

            HttpHeaders responseHeaders = new HttpHeaders();
            List<MediaType> medias = new ArrayList<>();
            medias.add(MediaType.ALL);
            responseHeaders.setAccept(medias);
            return new ResponseEntity<>(moviePage, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //future me please dont add more than 120 movies
    @PostMapping("/loadDatabase")
    public String reloadDatabase(@RequestParam(defaultValue = "50") HashMap<String, String> countries){
        movieService.reloadDatabase(countries);
        String[] response = {""};
        countries.forEach((country, count) ->{
            response[0] += count + " " + country + ",";
        });
        if (response[0].equals("")) return "no param found, deleting database";
        return response[0];
    }
    //future me please dont add more than 120 movies
    @PostMapping("/loadDatabase/{id}")
    public ResponseEntity<Movie> reloadDatabase(@PathVariable Long id){
        return new ResponseEntity<>(movieService.addMovie(id), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id){
        Long res = movieService.deleteMovieByID(id);
        if (res == 1) return new ResponseEntity<>("Deleted", HttpStatus.OK);
        else return new ResponseEntity<>("Not deleted", HttpStatus.NOT_FOUND);
    }
}
