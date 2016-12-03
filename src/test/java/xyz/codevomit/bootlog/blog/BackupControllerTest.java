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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import xyz.codevomit.bootlog.entity.Post;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "test.db.bootstrap=true")
@AutoConfigureMockMvc
@Slf4j
public class BackupControllerTest
{
    @Autowired
    MockMvc mockMvc;
    
    public BackupControllerTest()
    {
    }
    
    @Test
    public void testLandwithoutAuth() throws Exception
    {
        mockMvc.perform(get("/backup")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "merka", password = "merka")
    public void testLand() throws Exception
    {
        mockMvc.perform(get("/backup")).andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "merka", password = "merka")
    public void testExport() throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(post("/backup/export").with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        
        log.info("Here's the response content: {}", mvcResult.getResponse()
                .getContentAsString());
        
        List<Post> deserializedPosts = mapper.readValue(mvcResult.getResponse()
                .getContentAsString()
                , new TypeReference<List<Post>>(){});
        
        assertFalse(deserializedPosts.isEmpty());
    }
}
