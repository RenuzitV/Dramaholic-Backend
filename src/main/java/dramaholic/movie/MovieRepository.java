package dramaholic.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, QuerydslPredicateExecutor<Movie> {
    Movie findFirstByDbID(Long dbID);
    Long deleteByDbID(Long dbID);
}