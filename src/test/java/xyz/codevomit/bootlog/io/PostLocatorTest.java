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
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.codevomit.bootlog.exception.PostFileAlreadyExistsException;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
public class PostLocatorTest
{   
    public static final String FILENAME = "test.txt";
    public static final String TEXT = "Just a plain test string";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    public PostLocatorTest()
    {
    }
    
    @Test
    public void testSavePostFile() throws IOException
    {
        File saved = saveTestFile(FILENAME);
        
        assertEquals(FILENAME, saved.getName());
        assertTrue(saved.exists());
        String writtenText = FileUtils.readFileToString(saved);
        assertEquals(TEXT, writtenText);        
    }
    
    private File saveTestFile(String filename) throws IOException
    {
        String text = TEXT;
        byte[] mockContent = text.getBytes();
        PostLocator locator = new PostLocator(tempFolder.getRoot().getAbsolutePath());
        
        File saved = locator.savePostFile(filename, mockContent);
        return saved;
    }
    
    @Test(expected = PostFileAlreadyExistsException.class)
    public void testSavePostFileDuplicated() throws IOException
    {
        saveTestFile("name.txt");
        saveTestFile("name.txt");
    }
}
