/*
 * Copyright (C) 2016 CodeVomit Productions
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package xyz.codevomit.bootlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.codevomit.bootlog.blog.PostLocator;
import xyz.codevomit.bootlog.repository.PostRepository;

/**
 *
 * @author merka
 */
@Configuration
public class BootlogConfiguration extends WebMvcConfigurerAdapter
{   
    @Value("${images.base.folder}")
    private String imagesBaseFolder;
    
    @Value("${posts.directory}")
    private String postsDirectoryPath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesBaseFolder);
    }
    
    @Bean
    @Autowired
     public BootlogBootstrapper bootlogBootstrapper(PostRepository postRepository)
     {
         BootlogBootstrapper bootstrapper = new BootlogBootstrapper(postRepository,
            postsDirectoryPath);
         bootstrapper.bootstrapDatabase();
         return bootstrapper;
     }
     
     @Bean
     public PostLocator postLocator()
     {
         return new PostLocator(postsDirectoryPath);
     }
}
