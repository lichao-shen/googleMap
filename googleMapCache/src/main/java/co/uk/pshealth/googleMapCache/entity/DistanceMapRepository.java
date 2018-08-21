package co.uk.pshealth.googleMapCache.entity;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistanceMapRepository extends CrudRepository<Test, Long> {

	//List<Test> getNames();
}
