package xyz.codevomit.bootlog.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author merka
 */
@Controller
@RequestMapping(path = "/blog")
@Slf4j
public class PostFrameController
{
    @RequestMapping(path = {"/", ""})
    public String postFrame(Model model)
    {
        log.info("Base URL requested, no specific post");
        model.addAttribute("prefix", "");
        return "post-frame";
    }
    
    @RequestMapping("/{postId}")
    public String showPost(@PathVariable(value = "postId")String postId,
        Model model)
    {
        log.info("Request post with id = " + postId);
        model.addAttribute("postId", postId);
        model.addAttribute("prefix", "../");
        return "post-frame";
    }
}
