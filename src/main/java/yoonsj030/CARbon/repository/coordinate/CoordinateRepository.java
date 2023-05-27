package yoonsj030.CARbon.repository.coordinate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoonsj030.CARbon.entity.coordinate.Coordinate;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
}
