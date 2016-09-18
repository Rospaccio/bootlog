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
package xyz.codevomit.bootlog.data;

import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.util.TestBuilder;

/**
 *
 * @author merka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class PostProviderTest
{
    @Autowired
    PostProvider provider;
    
    @Autowired
    PostRepository repository;
    
    private TestBuilder builder;
    
    public PostProviderTest()
    {
    }
    
    @PostConstruct
    public void init()
    {
        this.builder = new TestBuilder(repository);
    }
    
    @Test
    public void testFindLatestPosts()
    {
        assertNotNull(provider);
        int count = 10;
        List<Post> testPosts = builder.createAndSaveTestPosts(count);
        
        List<Post> latest = provider.findLatestPosts(4);
        
        assertEquals(4, latest.size());
        Post veryLatest = latest.get(0);
        LocalDate expectedLatestDate = builder.getBaseDateTime().plusDays(count - 1)
                .toLocalDate();
        log.info("expected = " + expectedLatestDate);
        log.info("got = " + veryLatest.getPublishedOn());
        assertTrue(veryLatest.getPublishedOn().toLocalDate().isEqual(expectedLatestDate));
        Post secondLatest = latest.get(1);
        assertFalse(veryLatest.getPublishedOn().isBefore(secondLatest.getPublishedOn()));
        
        latest.stream().forEach((post) -> log.info("Post has publish date = " + post.getPublishedOn()));
        //cleanup
        repository.delete(testPosts);
    }
}
