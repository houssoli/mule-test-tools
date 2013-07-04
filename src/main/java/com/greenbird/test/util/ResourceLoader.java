/*
 * Copyright 2013 greenbird Integration Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greenbird.test.util;

import com.greenbird.test.GreenbirdTestException;
import org.mule.util.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;
import java.net.URL;

public class ResourceLoader {
    private static final ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(
            new DefaultResourceLoader(Thread.currentThread().getContextClassLoader()));

    private ResourceLoader() {
        // NOP
    }

    public static String load(String location) {
        try {
            URL resourceUrl = resourcePatternResolver.getResource(location).getURL();
            return IOUtils.toString(resourceUrl, "UTF-8");
        } catch (IOException e) {
            throw new GreenbirdTestException(e);
        }
    }
}
