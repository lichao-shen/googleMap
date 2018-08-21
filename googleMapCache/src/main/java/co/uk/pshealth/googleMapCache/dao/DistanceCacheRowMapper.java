package co.uk.pshealth.googleMapCache.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

public class DistanceCacheRowMapper implements RowMapper<DistanceCacheDAO> {

	@Override
	public DistanceCacheDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DistanceCacheDAO dis = new DistanceCacheDAO();
		dis.setId(rs.getLong("id"));
		dis.setOriginPostcode(rs.getString("originPostcode"));
		dis.setDestinationPostcode(rs.getString("destinationPostcode"));
		dis.setTravelDistanceMetres(rs.getInt("travelDistanceMetres"));
		dis.setTravelTimeSeconds(rs.getInt("travelTimeSeconds"));
		dis.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime() );
		
		return dis;
	}

}
