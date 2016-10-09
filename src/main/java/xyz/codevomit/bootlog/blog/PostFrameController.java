package xyz.codevomit.bootlog.blog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.RedirectView;
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
                post.getText().getValue());
        
        log.debug("Parsed post = \n" + markdownContent);
        
        model.addAttribute(MARKDOWN_CONTENT, markdownContent);
        model.addAttribute("postId", postId);        
        model.addAttribute("prefix", "../");
        return POSTFRAME_VIEW;
    }
}
