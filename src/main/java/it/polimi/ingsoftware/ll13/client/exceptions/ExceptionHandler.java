package it.polimi.ingsoftware.ll13.client.exceptions;

import java.io.IOException;
import java.net.SocketException;

/**
 * This interface defines methods for handling different types
 * of exceptions that may occur during the execution of a client application.
 * Implementations of this interface can provide custom behavior based on the exception type,
 * such as logging, user notification, or recovery mechanisms.
 */
public interface ExceptionHandler {
    void handle(IOException e);
    void handle(ServerCrashError e);
    void handle(SocketConnectionError e);
    void handle(Exception e);
    void handle(SocketException e);
}
