package com.tutorialspoint;

import org.glassfish.jersey.server.ResourceConfig;
 
public class CustomApplication extends ResourceConfig
{
    public CustomApplication()
    {
        packages("com.tutorialspoint");
        //register(LoggingFilter.class);
        //register(GsonMessageBodyHandler.class);
 
        //Register Auth Filter here
        register(AuthenticationFilter.class);
    }
}