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
package xyz.codevomit.bootlog.markdown;

import lombok.extern.slf4j.Slf4j;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author merka
 */
@Slf4j
public class TestGenericCommonMarkUsage
{
    public static final String SHORT_MARKDOWN = "This is a **Markdown** `exmaple`";
    public TestGenericCommonMarkUsage()
    {
    }
    
    @Test
    public void test()
    {
        Parser parser = Parser.builder().build();
        Node node = parser.parse(SHORT_MARKDOWN);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String finalHTML = renderer.render(node);
        
        log.info("Parsed markdown produced: " + finalHTML);
        assertNotNull(finalHTML);
        assertTrue(finalHTML.contains("<"));
    }
}
