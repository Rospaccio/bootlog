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
package xyz.codevomit.bootlog.io;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import xyz.codevomit.bootlog.entity.Post;
import xyz.codevomit.bootlog.exception.PostFileAlreadyExistsException;

/**
 *
 * @author merka
 */
public class PostLocator
{
    private String postFolderPath;
    
    public PostLocator(String postFolderPath)
    {
        this.postFolderPath = postFolderPath;
    }
    
    public String readPostMarkdownContent(Post post) throws IOException
    {
        File file = fileInBaseFolder(post.getFilename());
        return FileUtils.readFileToString(file);
    }
    
    public File fileInBaseFolder(String filename)
    {
        File file = new File(postFolderPath + "/" + filename);
        return file;
    }
    
    public Node parsePostContent(Post post) throws IOException
    {
        String markdown = readPostMarkdownContent(post);
        Parser parser = Parser.builder().build();
        Node node = parser.parse(markdown);
        return node;
    }
    
    public String renderPostContentToHtml(Post post) throws IOException
    {
        Node node = parsePostContent(post);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String renderedHtml = renderer.render(node);
        return renderedHtml;
    }

    public File savePostFile(String filename, byte[] mockContent) throws IOException
    {
        File possiblyExisting = fileInBaseFolder(filename);
        if(possiblyExisting.exists())
        {
            throw new PostFileAlreadyExistsException(possiblyExisting.getAbsolutePath());
        }
        
        FileUtils.writeByteArrayToFile(possiblyExisting, mockContent);
        return possiblyExisting;
    }

    public void deletePostFile(Post toDelete)
    {
        File fileToDelete = fileInBaseFolder(toDelete.getFilename());
        fileToDelete.delete();
    }
}
