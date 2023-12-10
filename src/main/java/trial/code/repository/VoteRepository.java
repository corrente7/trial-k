package trial.code.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trial.code.model.Vote;
import java.util.List;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {
    List<Vote> findFirst3ByOrderByCreatedAtDesc();

}
