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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.util.TestBuilder;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BackupServiceTest
{
    @Autowired
    PostRepository postRepo;
    
    TestBuilder testBuilder;
    
    public BackupServiceTest()
    {
    }

    @Before
    public void setup()
    {
        testBuilder = new TestBuilder(postRepo);
    }
    
    @Test
    public void testBackupPostsToJSON() throws IOException
    {
        List<Post> testPosts = testBuilder.insertTestPosts(4);
        
        BackupService service = new BackupService(postRepo);
        
        String json = service.backupPostsToJSON();
        
        assertNotNull(json);
        log.info(json);
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        List<Post> readBackPosts = mapper.readValue(json, new TypeReference<List<Post>>(){});
        assertNotNull(readBackPosts);
        assertFalse(readBackPosts.isEmpty());
        assertEquals(4, readBackPosts.size());
        
        //cleanup
        postRepo.delete(testPosts);
    }
    
}
