package xyz.codevomit.bootlog;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class BootlogApplication extends SpringBootServletInitializer
{    
    @Value("${bootlog.profile.description:NA}")
    String profileDescription;
            
    public static final String GREETINGS = 
"  ____              _   _             \n" +
" |  _ \\            | | | |            \n" +
" | |_) | ___   ___ | |_| | ___   __ _ \n" +
" |  _ < / _ \\ / _ \\| __| |/ _ \\ / _` |\n" +
" | |_) | (_) | (_) | |_| | (_) | (_| |\n" +
" |____/ \\___/ \\___/ \\__|_|\\___/ \\__, |\n" +
"                                 __/ |\n" +
"                                |___/ ";
    
    public static final String GREETINGS_SUBTITLE = "No developers have been "
            + "harassed during the development of this software";

    public static final String DUMMY = "test";
    
    public static void main(String[] args)
    {
        SpringApplication.run(BootlogApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(BootlogApplication.class);
    }
    
    @PostConstruct
    public void printGreetings()
    {
        System.out.println();
        System.out.println(GREETINGS);
        System.out.println("Profile => " + profileDescription);
        System.out.println(GREETINGS_SUBTITLE);
        System.out.println(System.lineSeparator());
    }
}
/* This software strongly supports free marijuana. You cannot outlaw a plant. */