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
package xyz.codevomit.bootlog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.entity.Text;
import xyz.codevomit.bootlog.markdown.MarkdownUtilsTest;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE"
    , "test.db.bootstrap=false"})
public class PostServiceTest
{    
    @Autowired
    PostRepository postRepo;
    @Autowired
    PostService postService;
    
    public PostServiceTest()
    {
    }

    @Test
    public void testCreatePostWithText()
    {
        String url = "post-url";
        String title = "The Title";
        Post newPost = Post.builder()
                .sourceUrl(url)
                .title(title)
                .build();
        
        Post created = postService.createPostWithText(newPost, MarkdownUtilsTest.MARKDOWN_SAMPLE);
        
        assertNotNull(created);
        assertEquals(title, created.getTitle());
        assertEquals(url, created.getSourceUrl());
        assertNotNull(created.getText().getValue());
        String text = created.getText().getValue();
        assertEquals(MarkdownUtilsTest.MARKDOWN_SAMPLE, text);
    }

    @Test
    public void testGetTextValueByPost()
    {
        Post post = Post.builder()
                .title("test")
                .editedOn(LocalDateTime.now())
                .publishedOn(LocalDateTime.now())
                .sourceUrl("test-01")
                .build();
        Post saved = postService.createPostWithText(post, MarkdownUtilsTest.MARKDOWN_SAMPLE);
        assertNotNull(saved);
        
        Post found = postRepo.findOne(saved.getId());
        assertNotNull(found);
        assertNotNull(found.getText());
        
        String textValue = postService.getTextValueByPost(saved);
        
        assertNotNull(textValue);
        assertEquals(MarkdownUtilsTest.MARKDOWN_SAMPLE, textValue);
    }
    
    @Test
    public void testFindLatestPosts()
    {
        int count = 10;
        List<Post> posts = insertTestPosts(count);
        assertTrue(postRepo.count() >= count);
        
        List<Post> sortedDesc = postService.findLatestPosts(count);
        
        for(int i = 0; i < sortedDesc.size() - 1; i++)
        {
            assertTrue(sortedDesc.get(i).getPublishedOn().isAfter(
                    sortedDesc.get(i + 1).getPublishedOn()));
        }
        
        // cleanup
        postRepo.delete(posts);
    }

    @Test
    public void testFindLatestPost()
    {
        List<Post> posts = insertTestPosts(10);
        
        List<Post> allLatestOrdered = postService.findLatestPosts(10);
        Post latest = postService.findLatestPost();
        
        assertEquals(latest.getId(), allLatestOrdered.get(0).getId());
        
        // cleanup
        postRepo.delete(posts);
    }
    
    private List<Post> insertTestPosts(int count)
    {
        return IntStream.range(0, count)
                .mapToObj((i) -> newTestPost(i))
                .map((post) -> postRepo.save(post))
                .collect(Collectors.toList());
    }

    private Post newTestPost(int i)
    {
        LocalDateTime now = LocalDateTime.now();
        Text text = Text.builder()
                .value("Testing the sorting")
                .build();
        Post post = Post.builder()
                .editedOn(now)
                .publishedOn(now)
                .sourceUrl("test-url" + i)
                .title("The title of " + i)
                .text(text)
                .build();
        return post;
    }
}
