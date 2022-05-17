package dramaholic.comment;

import dramaholic.customer.Customer;
import dramaholic.movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
@Transactional
public class CommentController {
    private final MovieService movieService;
    private final CommentService commentService;

    @Autowired
    public CommentController(MovieService movieService, CommentService commentService) {
        this.movieService = movieService;
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<String> addComment(@RequestBody Comment comment){
        if (comment.getMovie() == null || comment.getMovie().getDbID() == null) return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        boolean ok = movieService.exists(comment.getMovie().getDbID());
        if (!ok) return new ResponseEntity<>("movie does not exist", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(commentService.addComment(comment.getMovie().getDbID(), comment), HttpStatus.OK);
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<String> voteComment(@PathVariable Long id, @RequestBody Customer customer){
//        if (!commentService.exists(id) || !customerService.checkCredentials(customer)) return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(commentService.vote(id, customer), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestBody Customer customer){
        return new ResponseEntity<>(commentService.deleteComment(id, customer), HttpStatus.OK);
    }
}
