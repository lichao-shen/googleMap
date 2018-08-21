package co.uk.pshealth.googleMapCache;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import co.uk.pshealth.googleMapCache.controller.MapCacheController;
import co.uk.pshealth.googleMapCache.controller.StaticController;
import co.uk.pshealth.googleMapCache.entity.DistanceMapRepository;
import co.uk.pshealth.googleMapCache.filter.*;
import co.uk.pshealth.googleMapCache.mapper.DistanceMatrixExceptionMapper;


@Configuration
@ApplicationPath("google")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(MapCacheController.class);
		register(JerseyAuthenticationFilter.class);
		register(DistanceMatrixExceptionMapper.class);
		//register(StaticController.class);
		//register(DistanceMapRepository.class);		
	}
}
