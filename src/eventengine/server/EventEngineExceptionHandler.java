package eventengine.server;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
 
@Provider
public class EventEngineExceptionHandler implements ExceptionMapper <EventEngineException> 
{
    @Override
    public Response toResponse(EventEngineException exception) 
    {
        return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();  
    }
}
