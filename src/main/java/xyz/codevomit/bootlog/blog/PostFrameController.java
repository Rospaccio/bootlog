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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.data.PostRepository;
import xyz.codevomit.bootlog.markdown.MarkdownUtils;
import xyz.codevomit.bootlog.service.PostService;

/**
 *
 * @author merka
 */
@Controller
@RequestMapping(path = "/blog")
@Slf4j
public class PostFrameController
{    
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    PostService postService;
    
    @Value("${analytics.enabled:true}")
    @Getter
    @Setter
    boolean analyticsEnabled;
    
    @ModelAttribute(name = "analyticsEnabled")
    public Boolean analyticsEnabled()
    {
        return analyticsEnabled;
    }
            
    
    @ModelAttribute(name = "posts")
    public List<Post> posts()
    {
        List<Post> posts = new ArrayList<>(postRepository.findAllByOrderByPublishedOnDesc());
        if(log.isDebugEnabled())
        {
            posts.stream().forEach((post) -> log.debug("{} \t\t->\t\t {}", post.getSourceUrl(), post.getPublishedOn()));
        }
        return posts;
    }
    
    @RequestMapping(path = {"/", ""})
    public String postFrame(Model model)
    {
        log.info("Base URL requested, no specific post");
        model.addAttribute("prefix", "");
        
        Post latest = postService.findLatestPost();
        if(latest == null)
        {
            return POSTFRAME_VIEW;
        }
        return "redirect:blog/" + latest.getSourceUrl();
    }
    public static final String POSTFRAME_VIEW = "post-frame";
    public static final String MARKDOWN_CONTENT = "markdownContent";
    
    @RequestMapping("/{postId}")
    public String showPost(@PathVariable(value = "postId")String postId,
        Model model) throws IOException
    {
        log.info("Request post with id = " + postId);
        
        Post post = postRepository.findBySourceUrl(postId);        
        String markdownContent = MarkdownUtils.renderMarkdownTextToHtml(
                post.getText().getContent());
        
        log.debug("Parsed post = \n" + markdownContent);
        
        model.addAttribute(MARKDOWN_CONTENT, markdownContent);
        model.addAttribute("postId", postId);        
        model.addAttribute("prefix", "../");
        return POSTFRAME_VIEW;
    }
}
