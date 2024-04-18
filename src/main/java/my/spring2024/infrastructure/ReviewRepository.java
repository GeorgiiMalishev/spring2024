package my.spring2024.infrastructure;

import my.spring2024.domain.Review;
import my.spring2024.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllBySender(User sender);
    List<Review> findAllByReceiver(User receiver);
}