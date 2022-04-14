package cs.event.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import cs.event.data.entity.LogEvent;

/**
 * JPA repository interface
 * 
 */
@Component
@Repository
public interface LogEventRepository extends CrudRepository<LogEvent, Integer> {

	/**
	 * Find from DB by log id
	 * 
	 * @param logId - log id for which the data to be retrieved
	 * @return LogEvent - Log event for the given log log id
	 */
	public LogEvent findByLogId(String logId);

}
