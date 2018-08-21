package co.uk.pshealth.googleMapCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import co.uk.pshealth.googleMapCache.db.DBServices;
import co.uk.pshealth.googleMapCache.service.GoogleDistanceAPI;

@SpringBootApplication
public class GoogleMapCacheApplication {


	public static void main(String[] args) {
		SpringApplication.run(GoogleMapCacheApplication.class, args);
		
	}
		
}
