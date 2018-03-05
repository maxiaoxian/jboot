/**
 * Copyright (c) 2015-2018, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot.web.session;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class JbootDefaultSessionWapper extends JbootSessionWapperBase implements HttpSession {


    private static LoadingCache<String, Map<String, Object>> sessions = Caffeine.newBuilder()
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(key -> new HashMap<>());


    @Override
    public Object getAttribute(String name) {
        Map<String, Object> map = sessions.getIfPresent(getOrCreatSessionId());
        return map == null ? null : map.get(name);
    }


    @Override
    public void setAttribute(String name, Object value) {
        Map<String, Object> map = sessions.get(getOrCreatSessionId());
        map.put(name, value);
    }


    @Override
    public void removeAttribute(String name) {
        Map<String, Object> map = sessions.getIfPresent(getOrCreatSessionId());
        if (map != null) {
            map.remove(name);
        }
    }


    @Override
    public Enumeration<String> getAttributeNames() {
        Map<String, Object> map = sessions.getIfPresent(getOrCreatSessionId());
        if (map == null) {
            map = new HashMap<>();
        }

        Set<String> keyset = map.keySet();
        final Iterator<String> iterator = keyset.iterator();
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
    }


    @Override
    public String[] getValueNames() {
        Map<String, Object> map = sessions.getIfPresent(getOrCreatSessionId());
        if (map == null) {
            map = new HashMap<>();
        }
        return map.keySet().toArray(new String[]{});
    }
}
