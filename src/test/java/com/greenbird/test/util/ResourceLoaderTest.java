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

import com.greenbird.test.GreeenbirdTestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.greenbird.test.coverage.CoverageUtil.callPrivateDefaultConstructor;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResourceLoaderTest {
    @Rule
    public ExpectedException exceptionExpectation = ExpectedException.none();

    @Test
    public void load_resourceFound_resourceLoaded() {
        assertThat(ResourceLoader.load("test-resource.txt"), is("TEST"));
    }

    @Test
    public void load_resourceNotFound_exceptionWrappedInTestException() {
        exceptionExpectation.expect(GreeenbirdTestException.class);
        ResourceLoader.load("unknown");
    }

    @Test
    public void callPrivateConstructorToSatisfyCoverageCheck() {
        callPrivateDefaultConstructor(ResourceLoader.class);
    }
}
