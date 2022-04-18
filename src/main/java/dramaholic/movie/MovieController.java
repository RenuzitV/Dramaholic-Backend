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
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
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
            @RequestParam(defaultValue = "id,desc") String[] sort){
        try {
            List<Order> orders = new ArrayList<>();
            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
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
            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
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
    public String reloadDatabase(){
        movieService.reloadDatabase();
        return "ok";
    }
}
