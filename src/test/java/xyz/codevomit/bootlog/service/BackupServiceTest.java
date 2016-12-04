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
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.data.TextRepository;
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
    
    @Autowired
    TextRepository textRepo;
    
    @Autowired
    PostService postService;
    
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
    
    @Test
    public void testImportPostContent() throws IOException
    {
        assertEquals(0, postRepo.count());
        InputStream jsonStream = getClass().getClassLoader().getResourceAsStream("json/bootlog-export.json");
        String json = IOUtils.toString(jsonStream);
        BackupService backupService = new BackupService(postRepo);
        
        backupService.importPostContent(json);
        
        List<Post> imported = postRepo.findAll();
        assertFalse(imported.isEmpty());
        assertEquals(5, imported.size());
        Post firstPost = imported.get(0);
        assertNotNull(firstPost.getPublishedOn());
        assertNotNull(firstPost.getSourceUrl());
               
        String firstContent = postService.getTextContentByPost(firstPost);
        assertTrue(StringUtils.isNotBlank(firstContent));
        
        //cleanup
        postRepo.deleteAll();
    }
}
