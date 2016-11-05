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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.entity.Text;
import xyz.codevomit.bootlog.service.PostService;

/**
 *
 * @author merka
 */
@Controller
@RequestMapping("/edit")
@Slf4j
public class EditController
{
    @Autowired
    PostService postService;
    
    @Autowired
    PostRepository postRepo;
    
    @GetMapping("/{id}")
    public String edit(@PathVariable(value = "id") Long id,
            Model model)
    {
        Post postToEdit = postRepo.findOne(id);
        postToEdit.getText().getValue();
        model.addAttribute("post", postToEdit);
        return "edit";
    }
    
    @PostMapping("/{id}")
    public RedirectView post(@PathVariable("id") Long id,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "text.value", required = true) String text)
    {
        Post postToEdit = postRepo.findOne(id);
        postToEdit.setTitle(title);
        postRepo.save(postToEdit);
        postService.changePostText(postToEdit, text);
        
        return new RedirectView("/posts");
    }
}
