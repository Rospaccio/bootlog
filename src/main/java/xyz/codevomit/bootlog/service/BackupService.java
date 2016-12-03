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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;

/**
 *
 * @author merka
 */
@Slf4j
public class BackupService
{
    PostRepository postRepository;
    
    public BackupService(PostRepository postRepository)
    {
        this.postRepository = postRepository;                
    }
    
    public String backupPostsToJSON() throws JsonProcessingException
    {
        List<Post> allPosts = postRepository.findAllForExport();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(allPosts);
    }
}
