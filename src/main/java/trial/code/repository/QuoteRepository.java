package trial.code.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import trial.code.model.Quote;
import java.util.List;

@Repository
@Transactional
public interface QuoteRepository extends CrudRepository<Quote, Long> {

    List<Quote> findTop10ByOrderByScoreAsc();

    List<Quote> findTop10ByOrderByScoreDesc();

    long count();
    Page<Quote> findAll(Pageable pageable);

}
