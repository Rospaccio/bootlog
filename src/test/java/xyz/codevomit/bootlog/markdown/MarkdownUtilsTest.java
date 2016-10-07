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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author merka
 */
@Slf4j
public class MarkdownUtilsTest
{
    public static final String MARKDOWN_SAMPLE = "# TEST! \n"
            + "## This is a markdown test \n"
            + "text followed by `inline code` and some of *this*"
            + " and **this**"
            + "\n"
            + "1. item \n"
            + "2. item \n"
            + "3. item \n"
            + "\n"
            + "### end";
    public MarkdownUtilsTest()
    {
    }

    @Test
    public void testRenderMarkdownTextToHtml() throws Exception
    {
        String html = MarkdownUtils.renderMarkdownTextToHtml(MARKDOWN_SAMPLE);
        assertNotNull(html);
        assertTrue(html.contains("<h1>"));
        assertTrue(html.contains("<ol>"));
        log.debug("Got HTML => {}", html);
    }    
}
