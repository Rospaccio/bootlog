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

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;

/**
 *
 * @author merka
 */
@Controller
@RequestMapping("/posts")
public class PostsController
{
    @Autowired
    PostRepository postRepo;
    
    @ModelAttribute(name = "allPosts")
    public List<Post> allPosts()
    {
        return postRepo.findAll();
    }
    
    @GetMapping(path = {"", "/"})
    public String posts()
    {
        return "posts";
    }
    
    @PostMapping(path = {"", "/"})
    public RedirectView delete(@RequestParam(name = "postId", required = true) Long postId)
    {
        Post toDelete = postRepo.findOne(postId);
        postRepo.delete(toDelete);
        RedirectView view = new RedirectView("/posts");
        return view;
    }
}
