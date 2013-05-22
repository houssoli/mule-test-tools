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

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class CausedByMatcher extends BaseMatcher<Throwable> {
    private Class<? extends Throwable> expectedWrapperType;
    private Class<? extends Throwable> expectedCauseType;
    private Throwable expectedCause;

    protected CausedByMatcher(Class<? extends Throwable> expectedWrapperType, Throwable expectedCause) {
        this.expectedWrapperType = expectedWrapperType;
        this.expectedCause = expectedCause;
    }

    protected CausedByMatcher(Class<? extends Throwable> expectedWrapperType, Class<? extends Throwable> expectedCauseType) {
        this.expectedWrapperType = expectedWrapperType;
        this.expectedCauseType = expectedCauseType;
    }

    @Override
    public boolean matches(Object item) {
        boolean match = false;
        if (item.getClass().equals(expectedWrapperType)) {
            Throwable cause = ((Throwable) item).getCause();
            if (testOnCauseIdentity()) {
                match = cause == expectedCause;
            } else {
                match = cause.getClass().equals(expectedCauseType);
            }
        }
        return match;
    }

    @Override
    public void describeTo(Description description) {
        if (testOnCauseIdentity()) {
            description.appendText(String.format("throwable of type %s with cause %s", expectedWrapperType.getName(), expectedCause));
        } else {
            description.appendText(String.format("throwable of type %s with cause of type %s", expectedWrapperType.getName(), expectedCauseType.getName()));
        }
    }

    private boolean testOnCauseIdentity() {
        return expectedCause != null;
    }
}

