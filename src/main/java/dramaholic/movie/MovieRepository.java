package dramaholic.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, QuerydslPredicateExecutor<Movie>, CrudRepository<Movie, Long> {
    Movie findFirstByDbID(Long dbID);
    @Transactional
    Long deleteByDbID(Long dbID);
}