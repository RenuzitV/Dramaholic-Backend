package dramaholic.comment;

import dramaholic.customer.Customer;
import dramaholic.customer.CustomerService;
import dramaholic.movie.Movie;
import dramaholic.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    private final MovieRepository movieRepository;
    private final CommentRepository commentRepository;
    private final CustomerService customerService;

    @Autowired
    public CommentService(MovieRepository movieRepository, CommentRepository commentRepository, CustomerService customerService) {
        this.movieRepository = movieRepository;
        this.commentRepository = commentRepository;
        this.customerService = customerService;
    }

    public String addComment(Long dbID, Comment comment) {
        try {
            Movie movie = movieRepository.findFirstByDbID(dbID);

            Customer customerFromCredentials = customerService.getCustomerFromCredentials(comment.getUser());
            if (customerFromCredentials == null) return "invalid credentials";
            comment.setUser(customerFromCredentials);

            movie.addComment(comment);
            movieRepository.save(movie);
            return "created";
        } catch (Exception e){
            return "failed";
        }
    }

    public boolean exists(Long id) {
        return commentRepository.existsById(id);
    }

    public String deleteComment(Long id, Customer customer) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) return "no such comment";
        Comment comment = optionalComment.get();

        if (!comment.getUser().checkCredentials(customer)) return "invalid credentials";

        Movie movie = comment.getMovie();
        movie.getComments().remove(comment);

        movieRepository.save(movie);
        commentRepository.deleteById(id);
        return "deleted";
    }

    public String vote(Long id, Customer customer) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) return "no such comment";
        Comment comment = optionalComment.get();

        Customer customerFromCredentials = customerService.getCustomerFromCredentials(customer);
        if (customerFromCredentials == null) return "no such customer";

        if (comment.getUpvoters().contains(customerFromCredentials)) {
            comment.removeUpvoter(customerFromCredentials);
            return "removed vote";
        }
        comment.addUpvoter(customerFromCredentials);
        return "voted";
    }
}
