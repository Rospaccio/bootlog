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
package xyz.codevomit.bootlog.entity;

import java.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.codevomit.bootlog.repository.PostRepository;

/**
 *
 * @author merka
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class PostTest
{
    @Autowired
    PostRepository postRepository;
    
    public PostTest()
    {
    }

    @Test
    public void testInit()
    {
        assertNotNull(postRepository);
    }
    
    @Test
    public void testCRUD()
    {
        Post post = Post.builder()
                .title("Rospo")
                .sourceUrl("www.sticazzi.com")
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
}
