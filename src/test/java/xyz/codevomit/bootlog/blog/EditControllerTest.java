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

import java.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.springframework.test.web.servlet.MvcResult;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.entity.Text;
import xyz.codevomit.bootlog.service.PostService;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE"})
@AutoConfigureMockMvc
public class EditControllerTest
{
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    PostRepository postRepo;
    
    @Autowired
    PostService postService;
    
    public EditControllerTest()
    {
    }

    @Test
    public void testGetUnauthorized() throws Exception
    {
        mockMvc.perform(get("/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
    
    @Test
    @WithMockUser(username = "merka", password = "merka")
    public void testGet() throws Exception
    {
        Post post = saveTestPostToEdit();
        Long id = post.getId();
                
        MvcResult result = mockMvc.perform(get("/edit/" + id)).andExpect(status()
                .is2xxSuccessful())
                .andExpect(model().attributeExists("post"))
                .andReturn();
        Post editingPost = (Post)result.getModelAndView().getModel().get("post");
        assertEquals(id, editingPost.getId());
        
        String textValue = editingPost.getText().getValue();
        assertNotNull(textValue);
        
        // cleanup
        postRepo.delete(post);
    }

    private Post saveTestPostToEdit()
    {
        Post post = Post.builder()
                .publishedOn(LocalDateTime.now())
                .sourceUrl("test-post-01")
                .title("Test Post")
                .build();
        return postService.createPostWithText(post, "Blah blah blah blah");
    }
    
    @Test
    @WithMockUser(username = "merka", password = "merka")
    public void testPost() throws Exception
    {
        Post post = saveTestPostToEdit();
        String changedTitle = "What a beatiful new title";
        String changedText = "New shining fancy text!";
        
        mockMvc.perform(post("/edit/" + post.getId()).with(csrf())
                .param("title", changedTitle)
                .param("text.value", changedText))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        
        Post shouldHaveNewTitle = postRepo.findOne(post.getId());
        String shouldBeChanged = postService.getTextValueByPost(post);
        assertEquals(changedTitle, shouldHaveNewTitle.getTitle());
        assertEquals(changedText, shouldBeChanged);
        
        // cleanup
        postRepo.delete(post);
    }
    
}
