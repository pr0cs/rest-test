package com.tutorialspoint;

import org.glassfish.jersey.server.ResourceConfig;
 
public class CustomApplication extends ResourceConfig
{
    public CustomApplication()
    {
        packages("com.tutorialspoint");
 
        //Register Auth Filter here
        register(AuthenticationFilter.class);
    }
}