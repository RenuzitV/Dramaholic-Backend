package dramaholic.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, QuerydslPredicateExecutor<Movie>, CrudRepository<Movie, Long> {
    Movie findFirstByDbID(Long dbID);
}