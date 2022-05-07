package dramaholic.movie;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, QuerydslPredicateExecutor<Movie> {
    Movie findFirstByDbID(Long dbID);
    Long deleteByDbID(Long dbID);
}