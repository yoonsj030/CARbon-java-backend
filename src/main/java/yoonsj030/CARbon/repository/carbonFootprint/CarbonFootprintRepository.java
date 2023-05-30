package yoonsj030.CARbon.repository.carbonFootprint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yoonsj030.CARbon.entity.carbonFootprint.CarbonFootprint;

import java.util.List;

@Repository
public interface CarbonFootprintRepository extends JpaRepository<CarbonFootprint, Long> {
    @Query("SELECT cf.year, cf.month, cf.day, SUM(cf.emissions) AS totalEmissions " +
            "FROM CarbonFootprint cf " +
            "WHERE cf.user.id = :userId AND cf.year = :year " +
            "GROUP BY cf.year, cf.month, cf.day " +
            "ORDER BY cf.year, cf.month, cf.day")
    List<Object[]> getYearlyEmissionsByUser(@Param("userId") Long userId, @Param("year") int year);
}
