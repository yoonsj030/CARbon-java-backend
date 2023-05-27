package yoonsj030.CARbon.repository.carbonFootprint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoonsj030.CARbon.entity.carbonFootprint.CarbonFootprint;

import java.util.List;

@Repository
public interface CarbonFootprintRepository extends JpaRepository<CarbonFootprint, Long> {
}
