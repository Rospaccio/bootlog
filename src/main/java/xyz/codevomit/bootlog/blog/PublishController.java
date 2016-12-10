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
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.service.PostService;

/**
 *
 * @author merka
 */
@Controller
@RequestMapping("/publish")
@Slf4j
public class PublishController
{
    @Autowired
    PostService postService;
    
    @ModelAttribute(name = "defaultPublishDate")
    public LocalDateTime defaultPublishDate()
    {
        return LocalDateTime.now()
                .withSecond(0)
                .withNano(0);
    }
    
    @RequestMapping(path = {""}, method = RequestMethod.GET)
    public String publish()
    {
        return "publish";
    }
    
    @PostMapping(path = "")
    public RedirectView publishNewPost(@RequestParam(name = "postFile", required = true) MultipartFile file,
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "url", required = true) String url,
            @RequestParam(name = "publishDate", required = false) 
                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                    LocalDateTime publishDate) throws IOException
    {
        String textValue = new String(file.getBytes());
        if (log.isDebugEnabled())
        {
            log.debug(textValue);
        }

        Post toCreate = Post.builder()
                .sourceUrl(url)
                .publishedOn(publishDate)
                .title(title)
                .build();
        postService.createPostWithText(toCreate, textValue);

        RedirectView redirectView = new RedirectView("posts");
        return redirectView;
    }
}
