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

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.markdown.MarkdownUtilsTest;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE")
public class PostServiceTest
{    
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
    public void testFindLatestPosts()
    {
    }

    @Test
    public void testFindLatestPost()
    {
    }
    
}
