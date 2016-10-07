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
package xyz.codevomit.bootlog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;

/**
 *
 * @author merka
 */
@Slf4j
public class TestBuilder
{
    @Getter
    PostRepository postRepository;
    
    @Getter
    final LocalDateTime baseDateTime =  LocalDateTime.of(2016, 9, 1, 12, 12);

    public TestBuilder(PostRepository repository)
    {
        this.postRepository =  repository;
    }
    
    public List<Post> createAndSaveTestPosts(int count)
    {
        return IntStream.range(0, count)
                .mapToObj((index) -> buildTestPost(index))
                .map((post) -> postRepository.save(post))
                .collect(Collectors.toList());
    }
    
    public Post buildTestPost(int index)
    {
        log.info("building test post for index " + index);
        log.info("publish date is going to be " + baseDateTime.plusDays(index));
        return Post.builder().publishedOn(baseDateTime.plusDays(index))
                .editedOn(baseDateTime.plusDays(index))
                .sourceUrl("test_" + index)
                .build();
    }
    
    public String toJSON(List<Post> posts) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(posts);
        return json;
    }
}
