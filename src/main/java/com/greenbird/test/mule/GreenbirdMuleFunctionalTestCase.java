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
import com.greenbird.test.util.ResourceLoader;
import org.junit.Before;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.construct.Flow;
import org.mule.tck.junit4.FunctionalTestCase;

public abstract class GreenbirdMuleFunctionalTestCase extends FunctionalTestCase {
    public static final int DEFAULT_MESSAGE_REQUEST_TIMEOUT = 1000;
    private MuleClient muleClient;

    @Before
    public void setUpMuleClient() {
        muleClient = new DefaultLocalMuleClient(muleContext);
    }

    protected MuleClient client() {
        return muleClient;
    }

    protected String load(String location) {
        return ResourceLoader.load(location);
    }

    protected void dispatch(String address, Object payload) {
        try {
            client().dispatch(address, payload, null);
        } catch (MuleException e) {
            throw new GreenbirdTestException(e);
        }
    }

    protected MuleMessage request(String address) {
        try {
            return client().request(address, getMessageRequestTimeout());
        } catch (MuleException e) {
            throw new GreenbirdTestException(e);
        }
    }

    protected Flow flow(String name) {
        try {
            return (Flow) getFlowConstruct(name);
        } catch (Exception e) {
            throw new GreenbirdTestException(e);
        }
    }

    protected MuleEvent event(Object payload) {
        try {
            return getTestEvent(payload);
        } catch (Exception e) {
            throw new GreenbirdTestException(e);
        }
    }

    protected <T> T bean(String componentName) {
        return muleContext.getRegistry().get(componentName);
    }

    protected int getMessageRequestTimeout() {
        return DEFAULT_MESSAGE_REQUEST_TIMEOUT;
    }
}
