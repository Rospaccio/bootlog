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
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.hibernate.Hibernate;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.service.PostService;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties =
{
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE",
    "test.db.bootstrap=true"
})
@AutoConfigureMockMvc
public class PostFrameControllerTest
{

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PostRepository postRepo;
    @Autowired
    PostService postService;
    @Autowired
    PostFrameController controller;

    public PostFrameControllerTest()
    {
    }

    @Test
    public void testPostFrame() throws Exception
    {
        LocalDateTime now = LocalDateTime.now();
        Post latestPost = Post.builder()
                .editedOn(now)
                .publishedOn(now)
                .sourceUrl("blah")
                .title("Blah title")
                .build();
        postRepo.save(latestPost);

        mockMvc.perform(get("/blog")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("blog/blah"));

        // cleanup
        postRepo.delete(latestPost);
    }

    @Test
    @Ignore
    public void testShowPostNotPresent() throws Exception
    {
        if (postRepo.count() > 0)
        {
            postRepo.deleteAll();
        }
        assertTrue(postRepo.count() == 0);
        MvcResult result = mockMvc.perform(get("/blog")).andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("analyticsEnabled", true))
                .andReturn();
        assertNull(result.getModelAndView().getModel().get(PostFrameController.MARKDOWN_CONTENT));
    }

    @Test
    @WithMockUser(username = "merka", password = "merka")
    @Ignore
    public void testAnalyticsDisabledWithUserLogged() throws Exception
    {
        String url = firstValidUrl();

        mockMvc.perform(get("/blog/" + url)).andExpect(status().isOk())
                .andExpect(model().attribute("analyticsEnabled", false));
    }
    
    private String firstValidUrl()
    {
        return postService.findLatestPost().getSourceUrl();
    }

    @Test
    public void testAnalyticsEnabledWithoutUserLogged() throws Exception
    {
        MvcResult result = mockMvc.perform(get("/blog/" + firstValidUrl()))
                .andExpect(status().isOk())
                .andReturn();
                
        Object analyticsEnabled = result.getModelAndView().getModel().get("analyticsEnabled");
        assertNotNull(analyticsEnabled);
        assertEquals(true, analyticsEnabled);
    }

//    @Test
//    public void testShowPostAnalyticsNotEnabled() throws Exception
//    {
//        Post post = postRepo.findAll().get(0);
//        String url = post.getSourceUrl();
//        assertFalse(controller.isAnalyticsEnabled());
//        MvcResult result = mockMvc.perform(get("/blog/" + url))
//                .andExpect(status().isOk())
//                .andReturn();
//    }
}
