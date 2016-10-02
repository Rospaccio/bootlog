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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.codevomit.bootlog.data.PostProvider;
import xyz.codevomit.bootlog.entity.Post;

/**
 *
 * @author merka
 */
@Controller
@RequestMapping("/")
public class IndexController
{
    @Autowired
    PostProvider postProvider;
    
    @ModelAttribute(name = "latestPosts")
    public List<Post> latestPosts()
    {
        return postProvider.findLatestPosts(3);
    }
    
    @RequestMapping(path = {"/", ""})
    public String index()
    {
        return "index";
    }
}
