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

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.entity.Text;

/**
 *
 * @author merka
 */
public class PostService
{
    PostRepository postRepo;
    
    public PostService(PostRepository postRepository)
    {
        this.postRepo = postRepository;
    }
            
    public Post createPostWithText(Post post, String textValue)
    {
        Text text = Text.builder()
                .post(post)
                .value(textValue)
                .build();
        post.setText(text);
        return postRepo.save(post);        
    }
    
    public List<Post> findLatestPosts(int limit)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "publishedOn");
        Pageable pageable = new PageRequest(0, limit, sort);
        Page<Post> latestPosts = postRepo.findAll(pageable);
        return latestPosts.getContent();
    }
    
        public Post findLatestPost()
    {
        List<Post> oneLatest = findLatestPosts(1);
        if(oneLatest.isEmpty())
        {
            return null;
        }
        return oneLatest.get(0);
    }
}
