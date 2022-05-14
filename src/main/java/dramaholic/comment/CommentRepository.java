package dramaholic.comment;

import dramaholic.comment.Comment;
import dramaholic.customer.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    List<Comment> deleteCommentsByUser_Id(Long id);

}
