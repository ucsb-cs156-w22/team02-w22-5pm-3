package edu.ucsb.cs156.team02.repositories;

import edu.ucsb.cs156.team02.entities.CollegiateSubreddit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollegiateSubredditRepository extends CrudRepository<CollegiateSubreddit, Long> {
  Optional<CollegiateSubreddit> findByName(String name);
  Optional<CollegiateSubreddit> findBySubreddit(String subreddit);
  Optional<CollegiateSubreddit> findById(Long id);
}