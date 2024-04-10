package controller;

import com.sun.net.httpserver.HttpHandler;


public interface PathHandler extends HttpHandler {

    String getPath();

}
