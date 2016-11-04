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

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.service.PostService;
import xyz.codevomit.bootlog.util.TestBuilder;

/**
 *
 * @author merka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE", "test.db.bootstrap=false"})
@Slf4j
public class BootlogBootstrapperTest
{    
    @Autowired
    PostRepository postRepo;
    
    @Autowired
    PostService postService;
    
    @Value("${posts.directory}")
    String postTestDirectory;
    
    private TestBuilder testBuilder;
    
    public BootlogBootstrapperTest()
    {
    }

    @PostConstruct
    public void init()
    {
        this.testBuilder = new TestBuilder(postRepo);
    }
    
    /**
     * Test of bootstrapDatabase method, of class BootlogBootstrapper.
     */
    @Test
    public void testBootstrapDatabase()
    {
        postRepo.deleteAll();
        assertEquals(0, postRepo.count());
        assertNotNull(postRepo);
        BootlogBootstrapper bootstrapper = new BootlogBootstrapper(postRepo,
                postService, postTestDirectory);
        
         bootstrapper.bootstrapDatabase();
         
         assertNotEquals(0, postRepo.count());
         postRepo.findAll().stream().forEach((p) -> log.info("found post with url = " + p.getSourceUrl()));
    }
    
    @Test
    public void testDump() throws JsonProcessingException
    {
        List<Post> created = testBuilder.createAndSaveTestPosts(4);
        
        String json = testBuilder.toJSON(created);
        log.info(json);
        
        // cleanup
        postRepo.delete(created);
    }
    
    @Test
    @Ignore
    public void testReadPostsFromJson() throws JsonProcessingException
    {
        List<Post> created = testBuilder.createAndSaveTestPosts(4);
        String json = testBuilder.toJSON(created);
    }
}
