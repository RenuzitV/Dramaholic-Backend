package dramaholic.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
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
    public ResponseEntity<String> addCustomer(@ModelAttribute Movie movie) {
        if (movieService.isValid(movie)) {
            movieService.addMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
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
            @RequestParam(defaultValue = "0") Long episodesGT,
            @RequestParam(defaultValue = "50") Long episodesLTE){
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

            Page<Movie> moviePage = movieService.find(title, rateGT, rateLTE, episodesGT, episodesLTE, pagingSort);

            HttpHeaders responseHeaders = new HttpHeaders();
            List<MediaType> medias = new ArrayList<>();
            medias.add(MediaType.ALL);
            responseHeaders.setAccept(medias);
            return new ResponseEntity<>(moviePage, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loadDatabase")
    public String reloadDatabase(
            @RequestParam(defaultValue = "50") int g,
            @RequestParam(defaultValue = "50") int ko){
        movieService.reloadDatabase(g, ko);
        return g + " general " + ko + " ko";
    }
}
