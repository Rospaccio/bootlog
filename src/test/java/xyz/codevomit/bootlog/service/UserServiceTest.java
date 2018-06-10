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
package xyz.codevomit.bootlog.service;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author merka
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest
{
    @Autowired
    UserService userService;
    
    public UserServiceTest()
    {
    }

    @Test
    public void testIsUserLogged()
    {
        boolean isUserLogged = userService.isUserLogged();
        
        assertFalse(isUserLogged);
    }
    
    @Test
    public void testIsUserLoggedWithUser()
    {
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        assertTrue(userService.isUserLogged());
        
        // cleanup        
        SecurityContextHolder.getContext().setAuthentication(null);
    }
    
}
