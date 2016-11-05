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
import javax.transaction.Transactional;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.data.TextRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.entity.Text;
import xyz.codevomit.bootlog.exception.TextNotFoundException;

/**
 *
 * @author merka
 */
public class PostService
{

    PostRepository postRepo;
    TextRepository textRepository;

    public PostService(PostRepository postRepository,
            TextRepository textRepository)
    {
        this.postRepo = postRepository;
        this.textRepository = textRepository;
    }

    public Post createPostWithText(Post post, String textValue)
    {
        Text text = Text.builder()
                .post(post)
                .value(textValue)
                .build();
        text.setPost(post);
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
        if (oneLatest.isEmpty())
        {
            return null;
        }
        return oneLatest.get(0);
    }
    
    public Text findTextByPost(Post post)
    {
        Text textObject = textRepository.findOneByPost(post);
        if(textObject == null)
        {
            throw new TextNotFoundException("No text associated for post with id " + post.getId());
        }
        return textObject;
    }
    
    public String getTextValueByPost(Post post)
    {
        Text textObject = findTextByPost(post);
        return textObject.getValue();
    }
    
    @Transactional
    public void changePostText(Post savedWithWrongText, String newText)
    {
        Post retrievedPost = postRepo.findOne(savedWithWrongText.getId());
        Text amendingText = findTextByPost(retrievedPost);
        amendingText.setValue(newText);
        textRepository.save(amendingText);
    }   
    
}
