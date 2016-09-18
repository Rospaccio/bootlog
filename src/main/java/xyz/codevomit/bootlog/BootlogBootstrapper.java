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
package xyz.codevomit.bootlog;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.data.PostRepository;

/**
 *
 * @author merka
 */
public class BootlogBootstrapper
{
    private String postsDirectoryPath;
    
    private PostRepository postRepository;
    
    public BootlogBootstrapper(PostRepository postRepository)
    {
        this(postRepository, null);
    }
    
    public BootlogBootstrapper(PostRepository postRepository, String postDirectoryPath)
    {
        this.postRepository = postRepository;
        if(StringUtils.isNotBlank(postDirectoryPath))
        {
            this.postsDirectoryPath = postDirectoryPath;
        }
    }
    
    public void bootstrapDatabase()
    {
        if(postRepository.count() == 0)
        {
            insertExistingPostsInDatabase();
        }
    }

    private void insertExistingPostsInDatabase()
    {
        File baseDirectory = new File(postsDirectoryPath);
        if(!baseDirectory.exists())
        {
            throw new IllegalStateException("Post folder '" + postsDirectoryPath 
                    + "' not found");
        }
        if(baseDirectory.isFile())
        {
            throw new IllegalStateException("Post folder path '" + postsDirectoryPath 
                    + "' correspond to a file, not a directory");
        }
        
        Collection<File> markdownFiles = FileUtils.listFiles(baseDirectory, 
                new String[]{"md"}, false);
        
        markdownFiles.stream().forEach((file) -> saveDatabaseEntryFor(file));
    }

    private void saveDatabaseEntryFor(File file)
    {
        try
        {
            LocalDateTime conventionalDate = LocalDateTime.of(2016, 1, 1, 0, 0);
            String url = file.getName().replace(".md", "");
            Post post = Post.builder()
                    .editedOn(conventionalDate)
                    .publishedOn(conventionalDate)
                    .sourceUrl(url)
                    .filename(file.getName())
                    .title(url)
                    .build();
            postRepository.save(post);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
