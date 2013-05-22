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

package com.greenbird.test.mule;

import com.greenbird.test.GreeenbirdTestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mule.api.construct.FlowConstruct;

import static com.greenbird.test.matchers.GreenbirdMatchers.causedBy;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class GreenbirdMuleFunctionalTestCaseTest extends GreenbirdMuleFunctionalTestCase {
    @Rule
    public ExpectedException exceptionExpectation = ExpectedException.none();

    private final RuntimeException muleException = new RuntimeException();
    private boolean muleFails;

    @Override
    protected String getConfigResources() {
        return "mule/test-mule-config.xml";
    }

    @Test
    public void load_resourceFound_resourceLoaded() {
        assertThat(load("test-resource.txt"), is("TEST"));
    }

    @Test
    public void load_resourceNotFound_exceptionWrappedInTestException() {
        exceptionExpectation.expect(GreeenbirdTestException.class);
        load("unknown");
    }

    @Test
    public void dispatchAndRequest_normal_messageReceived() throws Exception {
        dispatch("vm://test/in", "TEST");
        sleep(100);
        assertThat(request("vm://test/out").getPayloadAsString(), is("TEST"));
    }

    @Test
    public void dispatch_muleTrowsException_exceptionWrappedInTestException() {
        exceptionExpectation.expect(GreeenbirdTestException.class);
        dispatch("unknown://address", "TEST");
    }

    @Test
    public void request_muleTrowsException_exceptionWrappedInTestException() {
        exceptionExpectation.expect(GreeenbirdTestException.class);
        dispatch("vm://test/in", "TEST");
        request("unknown://address");
    }

    @Test
    public void flow_existingFound_flowLoaded() {
        assertThat(flow("TestFlow"), is(notNullValue()));
    }

    @Test
    public void flow_muleThrowsException_exceptionWrappedInTestException() {
        muleFails = true;
        exceptionExpectation.expect(causedBy(GreeenbirdTestException.class, muleException));
        flow("TestFlow");
    }

    @Test
    public void event_normal_returnsEventContainingGivenPayload() throws Exception {
        assertThat(event("TEST").getMessage().getPayloadAsString(), is("TEST"));
    }

    @Test
    public void bean_beanFound_returnsBeanFromSpringContext() throws Exception {
        assertThat(bean("testSpringComponent"), is(notNullValue()));
    }

    @Test
    public void bean_beanNotFound_returnsNull() throws Exception {
        assertThat(bean("unknown"), is(nullValue()));
    }

    @Override
    protected FlowConstruct getFlowConstruct(String flowName) throws Exception {
        if (muleFails) {
            throw muleException;
        } else {
            return super.getFlowConstruct(flowName);
        }
    }
}
