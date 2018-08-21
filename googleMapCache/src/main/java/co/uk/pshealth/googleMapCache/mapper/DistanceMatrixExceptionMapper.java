package co.uk.pshealth.googleMapCache.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;
import co.uk.pshealth.googleMapCache.exception.DistanceMatrixException;


@Component(value = "DistanceMatrixExceptionMapper")
@Provider
public class DistanceMatrixExceptionMapper implements ExceptionMapper<DistanceMatrixException> {

	@Override
	public Response toResponse(DistanceMatrixException e) {
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();

	}

}
