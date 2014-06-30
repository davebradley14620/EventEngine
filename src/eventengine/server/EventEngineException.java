package eventengine.server;

import java.io.Serializable;

public class EventEngineException extends Exception implements Serializable
{
    private static final long serialVersionUID = 1L;
    public EventEngineException() {
        super();
    }
    public EventEngineException(String msg)   {
        super(msg);
    }
    public EventEngineException(String msg, Exception e)  {
        super(msg, e);
    }
}
