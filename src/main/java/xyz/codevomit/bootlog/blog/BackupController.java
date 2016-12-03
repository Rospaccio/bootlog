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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.service.BackupService;

/**
 *
 * @author merka
 */
@Controller
@RequestMapping("/backup")
@Slf4j
public class BackupController
{
    @Autowired
    BackupService backupService;    
    
    @GetMapping(path = {""}) 
    public String land()
    {
        return "backup";
    }
    
    @PostMapping(path = {"export", "/export"})
    @ResponseBody
    public void export(HttpServletResponse response) throws JsonProcessingException, IOException
    {
        String serializedContent = backupService.backupPostsToJSON();
        response.setHeader("Content-Disposition", "attachment; filename=bootlog-export.json");
        FileCopyUtils.copy(new ByteArrayInputStream(serializedContent.getBytes()), response.getOutputStream());
    }
}
