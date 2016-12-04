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
package xyz.codevomit.bootlog.blog;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.service.PostService;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE"})
@AutoConfigureMockMvc
public class PublishControllerTest
{
    public static final String MARKDOWN_FRAGMENT = "MiBECoM - Next Steps\n" +
"====================\n" +
"\n" +
"## DONE\n" +
"* Comments ( **Disqus** ): works great!\n" +
"\n" +
"## TO DO\n" +
"\n" +
"* Google analytics: **each page should be reachable through its own URL**\n" +
"* Avoid cross site requests (www.codevomit.com / codevomit.com)\n" +
"* Export from / Import to WordPress";
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private PostService postService;
            
    public PublishControllerTest()
    {
    }

    @Test
    public void testGetPublishWithoutAuth() throws Exception
    {
        mockMvc.perform(get("/publish")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
    
    @Test
    public void testPublishNewPost() throws Exception
    {
        String url = "post-url";
        String filename = "test-markdown-file.md";
        
        Post post = postRepo.findBySourceUrl(url);
//        if(post != null)
//        {
//            postProvider.deletePostWithContent(post);
//        }
        
        byte[] postFileContent = MARKDOWN_FRAGMENT.getBytes(Charset.forName("UTF-8"));
        MockMultipartFile part = new MockMultipartFile("postFile", 
                filename, "octet/stream", postFileContent); 
        
        mockMvc.perform(fileUpload("/publish").file(part)
                .param("title", "post-title")
                .param("url", url)
                .param("publishDate", "2016-09-24T12:12")
                .with(user("merka").password("merka").roles("USER"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("posts"));
        
        Post created = postRepo.findBySourceUrl(url);
        assertNotNull(created);
        assertEquals("post-title", created.getTitle());
        assertEquals(url, created.getSourceUrl());
        assertEquals(LocalDateTime.of(2016, 9, 24, 12, 12), created.getPublishedOn());
        Post foundAgain = postRepo.findOne(created.getId());
        assertNotNull(foundAgain);
        String text = postService.getTextContentByPost(foundAgain); // created.getText().getValue();
        assertEquals(MARKDOWN_FRAGMENT, text);
        
        // cleanup
//        postProvider.deletePostWithContent(created);
    }
}
