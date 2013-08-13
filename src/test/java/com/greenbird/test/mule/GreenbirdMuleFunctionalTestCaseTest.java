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

import com.greenbird.test.GreenbirdTestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mule.api.MuleMessage;
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
        exceptionExpectation.expect(GreenbirdTestException.class);
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
        exceptionExpectation.expect(GreenbirdTestException.class);
        dispatch("unknown://address", "TEST");
    }

    @Test
    public void send_success_payloadReturned() {
        MuleMessage testMessage = send("vm://test/synchronous/in", "TEST");
        assertThat(testMessage.getPayload().toString(), is("TEST"));
    }

    @Test
    public void send_muleTrowsException_exceptionWrappedInTestException() {
        exceptionExpectation.expect(GreenbirdTestException.class);
        send("unknown://test/synchronous/in", "TEST");
    }

    @Test
    public void request_muleTrowsException_exceptionWrappedInTestException() {
        exceptionExpectation.expect(GreenbirdTestException.class);
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
        exceptionExpectation.expect(causedBy(GreenbirdTestException.class, muleException));
        flow("TestFlow");
    }

    @Test
    public void event_normal_eventContainingGivenPayloadReturned() throws Exception {
        assertThat(event("TEST").getMessage().getPayloadAsString(), is("TEST"));
    }

    @Test
    public void bean_beanFoundByName_beanReturned() {
        assertThat(bean("testSpringComponent"), is(notNullValue()));
    }

    @Test
    public void bean_beanNotFoundByName_nullReturned() {
        assertThat(bean("unknown"), is(nullValue()));
    }

    @Test
    public void bean_beanFoundByType_beanReturned() {
        assertThat(bean(TestSpringComponent.class), is(notNullValue()));
    }

    @Test
    public void bean_moreThanOneBeanFoundByType_beanReturned() {
        exceptionExpectation.expect(GreenbirdTestException.class);
        exceptionExpectation.expectMessage("More than one object of type");
        bean(DuplicateTestSpringComponent.class);
    }

    @Test
    public void bean_beanNotFoundByType_nullReturned() {
        assertThat(bean(String.class), is(nullValue()));
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
