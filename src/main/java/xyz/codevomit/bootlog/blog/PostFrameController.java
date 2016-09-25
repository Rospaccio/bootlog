package xyz.codevomit.bootlog.blog;

import xyz.codevomit.bootlog.io.PostLocator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.data.PostRepository;

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
    PostLocator postLocator;
    
    @ModelAttribute(name = "posts")
    public List<Post> posts()
    {
        log.info("Retrieving posts");
        List<Post> posts = new ArrayList<>(postRepository.findAll());
        log.info("Found " + posts.size() + " elements");
        return posts;
    }
    
    @RequestMapping(path = {"/", ""})
    public String postFrame(Model model)
    {
        log.info("Base URL requested, no specific post");
        model.addAttribute("prefix", "");
        return "post-frame";
    }
    
    @RequestMapping("/{postId}")
    public String showPost(@PathVariable(value = "postId")String postId,
        Model model) throws IOException
    {
        log.info("Request post with id = " + postId);
        
        Post post = postRepository.findBySourceUrl(postId);        
        String markdownContent = postLocator.renderPostContentToHtml(post);
        
        log.debug("Parsed post = \n" + markdownContent);
        
        model.addAttribute("markdownContent", markdownContent);
        model.addAttribute("postId", postId);        
        model.addAttribute("prefix", "../");
        return "post-frame";
    }
}
