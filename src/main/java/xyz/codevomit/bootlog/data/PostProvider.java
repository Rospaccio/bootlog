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
package xyz.codevomit.bootlog.data;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import xyz.codevomit.bootlog.io.PostLocator;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.exception.BootlogException;

/**
 *
 * @author merka
 */
@Service
@Transactional
@Slf4j
public class PostProvider
{    
    private PostRepository postRepository;    
    private PostLocator postLocator;
    
    @Autowired
    public PostProvider(PostRepository postRepository,
            PostLocator postLocator)
    {
        this.postRepository = postRepository;
        this.postLocator = postLocator;
    }
    
    public List<Post> findLatestPosts(int limit)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "publishedOn");
        Pageable pageable = new PageRequest(0, limit, sort);
        Page<Post> latestPosts = postRepository.findAll(pageable);
        return latestPosts.getContent();
    }
    
    public Post createPostWithContent(Post post, String text)
    {
        return createPostWithContent(post, text.getBytes());
    }
    
    public Post createPostWithContent(Post post, byte[] content)
    {
       if(StringUtils.isBlank(post.getFilename()))
        {
            throw new IllegalArgumentException("Post.filename cannot be black");
        }
        try
        {
            postLocator.savePostFile(post.getFilename(), content);
        }
        catch(IOException io)
        {
            log.error("Impossible to save the post file due to a IO Exception", io);
            throw new BootlogException(io);
        }
        Post saved = postRepository.save(post);
        return saved;
    }

    public void deletePostWithContent(Post toDelete)
    {
        postLocator.deletePostFile(toDelete);
        postRepository.delete(toDelete);
    }

    public Post findLatestPost()
    {
        Order order = new Sort.Order(Sort.Direction.DESC, "publishedOn");
        List<Post> oneLatest = findLatestPosts(1);
        if(oneLatest.isEmpty())
        {
            return null;
        }
        return oneLatest.get(0);
//        return  findLatestPosts(1).get(0);
    }
}
