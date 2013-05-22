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

package com.greenbird.test.matchers;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.greenbird.test.coverage.CoverageUtil.callPrivateDefaultConstructor;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GreenbirdMatchersTest {
    private final Exception testException = new Exception();

    @Test
    public void causedByMatches_wrongWrapperExceptionType_noMatch() {
        assertThat(GreenbirdMatchers.causedBy(RuntimeException.class, testException)
                .matches(new Exception()), is(false));
    }

    @Test
    public void causedByMatches_correctWrapperExceptionTypeWrongCause_noMatch() {
        assertThat(GreenbirdMatchers.causedBy(RuntimeException.class, testException)
                .matches(new RuntimeException(new Exception())), is(false));
    }

    @Test
    public void causedByMatches_correctWrapperExceptionTypeWrongCauseType_noMatch() {
        assertThat(GreenbirdMatchers.causedBy(RuntimeException.class, testException.getClass())
                .matches(new RuntimeException(new RuntimeException())), is(false));
    }

    @Test
    public void causedByMatches_correctWrapperExceptionTypeAndCause_matchOk() {
        assertThat(GreenbirdMatchers.causedBy(RuntimeException.class, testException)
                .matches(new RuntimeException(testException)), is(true));
    }

    @Test
    public void causedByMatches_correctWrapperExceptionTypeAndCauseType_matchOk() {
        assertThat(GreenbirdMatchers.causedBy(RuntimeException.class, testException.getClass())
                .matches(new RuntimeException(testException)), is(true));
    }

    @Test
    public void causedByDescribeTo_testingOnCauseIdentity_messageAsExpected() {
        Description description = new StringDescription();
        GreenbirdMatchers.causedBy(RuntimeException.class, testException).describeTo(description);
        assertThat(description.toString(), containsString(RuntimeException.class.getName()));
        assertThat(description.toString(), containsString(testException.toString()));
    }

    @Test
    public void causedByDescribeTo_testingOnCauseType_messageAsExpected() {
        Description description = new StringDescription();
        GreenbirdMatchers.causedBy(RuntimeException.class, testException).describeTo(description);
        assertThat(description.toString(), containsString(RuntimeException.class.getName()));
        assertThat(description.toString(), containsString(testException.getClass().getName()));
    }

    @Test
    public void callPrivateConstructorToSatisfyCoverageCheck() {
        callPrivateDefaultConstructor(GreenbirdMatchers.class);
    }
}
