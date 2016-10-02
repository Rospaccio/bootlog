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
package xyz.codevomit.bootlog.exception;

import lombok.Getter;

/**
 *
 * @author merka
 */
public class PostFileAlreadyExistsException extends BootlogException
{
    @Getter
    private String filename;
    
    public PostFileAlreadyExistsException(String filename)
    {
        this.filename = filename;
    }

    public PostFileAlreadyExistsException(String filename, String message)
    {
        super(message);
        this.filename = filename;
    }

    public PostFileAlreadyExistsException(String filename, String message, Throwable cause)
    {
        super(message, cause);
        this.filename = filename;
    }

    public PostFileAlreadyExistsException(String filename, Throwable cause)
    {
        super(cause);
        this.filename = filename;
    }

    @Override
    public String getMessage()
    {
        return String.format("File at path %s already exists", getFilename());
    }
    
    
}
