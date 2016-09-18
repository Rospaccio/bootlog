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

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xyz.codevomit.bootlog.entity.Post;

/**
 *
 * @author merka
 */
@Service
public class PostProvider
{
    private PostRepository postRepository;
    
    @Autowired
    public PostProvider(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    }
    
    public List<Post> findLatestPosts(int limit)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "publishedOn");
        Pageable pageable = new PageRequest(0, limit, sort);
        Page<Post> latestPosts = postRepository.findAll(pageable);
        return latestPosts.getContent();
    }
}
