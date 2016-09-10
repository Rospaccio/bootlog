package xyz.codevomit.bootlog.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
    public String postFrame()
    {
        return "post-frame";
    }
}
