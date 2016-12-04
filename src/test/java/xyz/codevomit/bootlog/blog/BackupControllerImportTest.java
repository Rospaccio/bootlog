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

import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import xyz.codevomit.bootlog.data.PostRepository;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "test.db.bootstrap=false")
@AutoConfigureMockMvc
@Slf4j
public class BackupControllerImportTest
{
    @Autowired 
    MockMvc mockMvc;
    
    @Autowired
    PostRepository postRepo;
            
    public BackupControllerImportTest()
    {
    }
    
    
    @Test
    @WithMockUser(username = "merka", password = "merka")
    public void testImport() throws Exception
    {
        assertTrue(postRepo.count() == 0);

        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/bootlog-export.json");
        byte[] jsonContent = IOUtils.toByteArray(stream);
        MockMultipartFile part = new MockMultipartFile("jsonContent",
                "bootlog-export.json", "octet/stream", jsonContent);

        mockMvc.perform(fileUpload("/backup/import").file(part).with(csrf())) // TODO set multipart content and input file
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/backup*"))
                .andExpect(flash().attributeExists("message"));

        assertEquals(5, postRepo.count());

        // cleanup
        postRepo.deleteAll();
    }
}
