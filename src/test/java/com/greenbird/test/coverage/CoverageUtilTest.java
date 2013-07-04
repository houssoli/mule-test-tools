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

package com.greenbird.test.coverage;

import com.greenbird.test.GreenbirdTestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.greenbird.test.coverage.CoverageUtil.callPrivateDefaultConstructor;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CoverageUtilTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void callPrivateDefaultConstructor_classWithDefaultPrivateConstructor_constructorIsCalled() {
        CoverageUtil.callPrivateDefaultConstructor(TestClassWithDefaultPrivateConstructor.class);
        assertThat(TestClassWithDefaultPrivateConstructor.instantiated, is(true));
    }

    @Test
    public void callPrivateDefaultConstructor_classWithoutDefaultPrivateConstructor_exceptionIsThrown() {
        expectedException.expect(GreenbirdTestException.class);
        expectedException.expectMessage("NoSuchMethodException");
        CoverageUtil.callPrivateDefaultConstructor(TestClassWithoutDefaultPrivateConstructor.class);
    }

    @Test
    public void getCoverageForDefaultPrivateConstructor() {
        callPrivateDefaultConstructor(CoverageUtil.class);
    }

    private static class TestClassWithDefaultPrivateConstructor {
        public static boolean instantiated;

        private TestClassWithDefaultPrivateConstructor() {
            instantiated = true;
        }
    }

    private static class TestClassWithoutDefaultPrivateConstructor {
        private TestClassWithoutDefaultPrivateConstructor(boolean test) {
        }
    }

}

