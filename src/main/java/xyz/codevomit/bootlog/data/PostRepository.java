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
package xyz.codevomit.bootlog.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.codevomit.bootlog.entity.Post;

/**
 *
 * @author merka
 */
public interface PostRepository extends JpaRepository<Post, Long>
{
    public Post findBySourceUrl(String sourceUrl);
    public List<Post> findAllByOrderByPublishedOnDesc();
    
    @Query("select p from Post p JOIN FETCH p.text")
    public List<Post> findAllForExport();
}
