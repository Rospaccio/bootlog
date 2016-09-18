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

import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.util.TestBuilder;

/**
 *
 * @author merka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class PostRepositoryTest
{
    private TestBuilder testBuilder;
    
    @Autowired
    PostRepository postRepository;
    
    public PostRepositoryTest()
    {
    }
    
    @PostConstruct
    public void init()
    {
        this.testBuilder = new TestBuilder(postRepository);
    }

    @Test
    public void testInit()
    {
        assertNotNull(postRepository);
        assertNotNull(testBuilder.getPostRepository());
    }
    
    @Test
    public void testCRUD()
    {
        Post post = Post.builder()
                .title("Rospo")
                .sourceUrl("www.sticazzi.com")
                .filename("asdf")
                .editedOn(LocalDateTime.now())
                .publishedOn(LocalDateTime.now())
                .build();
        
        Post saved = postRepository.save(post);
        assertNotNull(saved.getId());
        
        Post retrieved = postRepository.findOne(saved.getId());
        assertEquals(saved.getId(), retrieved.getId());
        
        postRepository.delete(saved);
        retrieved = postRepository.findOne(saved.getId());
        assertNull(retrieved);
    }
    
    public void testFindBySourceUrl()
    {
        String sourceUrl = "test-url";
        Post testPost = Post.builder().sourceUrl(sourceUrl).build();
        postRepository.save(testPost);
        
        Post retrieved = postRepository.findBySourceUrl(sourceUrl);
        
        assertNotNull(retrieved);
        assertEquals(testPost.getSourceUrl(), retrieved.getSourceUrl());
    }
    
    @Test
    public void findAllSortByDateDesc()
    {
        List<Post> testPosts = testBuilder.createAndSaveTestPosts(3);
        
        Sort sort = new Sort(Sort.Direction.DESC, "publishedOn");
        Pageable pageable = new PageRequest(0, 3, sort);
        Page<Post> firstThree = postRepository.findAll(pageable);
        assertEquals(3, firstThree.getNumberOfElements());
        List<Post> content = firstThree.getContent();
        Post firstPost = content.get(0);
        Post secondPost = content.get(1);
        Post thirdPost = content.get(2);
        assertFalse(firstPost.getPublishedOn().isBefore(secondPost.getPublishedOn()));
        assertFalse(secondPost.getPublishedOn().isBefore(thirdPost.getPublishedOn()));
        
        // cleanup
        postRepository.delete(testPosts);
    }
}
