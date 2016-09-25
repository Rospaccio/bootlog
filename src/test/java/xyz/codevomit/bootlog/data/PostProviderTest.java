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

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.io.PostLocator;
import xyz.codevomit.bootlog.util.TestBuilder;

/**
 *
 * @author merka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE"})
@Slf4j
public class PostProviderTest
{
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
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
    
    @Test
    public void testCreatePostWithContent()
    {
        String filename = "post2.md";
        String url = "post-url-test-2";
        Post post = Post.builder()
                .filename(filename)
                .publishedOn(LocalDateTime.now())
                .sourceUrl(url)
                .title("Title of the post")
                .build();
        String content = "This is a stupid test string";
        PostLocator tempLocator = new PostLocator(tempFolder.getRoot().getAbsolutePath());
        PostProvider tempProvider = new PostProvider(repository, tempLocator);
        
        Post created = tempProvider.createPostWithContent(post, content);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        Post retrieved = repository.findOne(created.getId());
        assertNotNull(retrieved);
        assertEquals(filename, retrieved.getFilename());
        assertEquals(url, retrieved.getSourceUrl());
        // cleanup
        repository.delete(retrieved);
    }
    
    @Test
    public void deletePostWithContent()
    {        
        String filename = "post3.md";
        String url = "post-url-test-3";
        Post post = Post.builder()
                .filename(filename)
                .publishedOn(LocalDateTime.now())
                .sourceUrl(url)
                .title("Title of the post")
                .build();
        String content = "This is a stupid test string";
        PostLocator tempLocator = new PostLocator(tempFolder.getRoot().getAbsolutePath());
        PostProvider tempProvider = new PostProvider(repository, tempLocator);
        Post created = tempProvider.createPostWithContent(post, content);
        
        tempProvider.deletePostWithContent(created);
        
        File shouldNotExist = tempLocator.fileInBaseFolder(post.getFilename());
        assertFalse(shouldNotExist.exists());
        assertNull(repository.findOne(created.getId()));
    }
}
