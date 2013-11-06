package com.greenbird.test.mule.xml;

import com.google.common.collect.ImmutableMap;
import com.greenbird.test.GreenbirdTestException;
import com.greenbird.xml.XPathRoot;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.greenbird.test.coverage.CoverageUtil.callPrivateDefaultConstructor;
import static com.greenbird.test.mule.xml.XmlTransformationTestUtil.transform;
import static com.greenbird.test.mule.xml.XmlTransformationTestUtil.verifyCurrentDate;
import static com.greenbird.test.mule.xml.XmlTransformationTestUtil.verifyEmptyNode;
import static com.greenbird.test.mule.xml.XmlTransformationTestUtil.verifyMissingNode;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class XmlTransformationTestUtilTest {
    private XPathRoot absenceRoot;

    @Before
    public void setUp() {
        Map<String, String> namespaces = ImmutableMap.of(
                "a", "http://test.a.com/schema/a/v1",
                "b", "http://test.b.com/schema/b/v2"
        );
        XPathRoot abwRoot = transform("/xml/source.xml", "/xsl/stylesheet.xsl", namespaces).rootFrom("a:ABWAbsenceRecord");
        absenceRoot = abwRoot.rootFrom("a:AbsenceRecord");
    }

    @Test
    public void verifyCurrentDate_elementContainsCurrentDate_verificationSucceeds() {
        verifyCurrentDate(absenceRoot, "a:EndDate");
    }

    @Test
    public void verifyCurrentDate_elementDoesNotContainsCurrentDate_verificationFails() {
        try {
            verifyCurrentDate(absenceRoot, "a:DateFrom");
            throw new GreenbirdTestException("Expected AssertionError.");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("a string starting with"));
        }
    }

    @Test
    public void verifyCurrentDate_elementNotFound_verificationFails() {
        try {
            verifyCurrentDate(absenceRoot, "a:unknown");
            throw new GreenbirdTestException("Expected AssertionError.");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("a string starting with"));
        }
    }

    @Test
    public void verifyEmptyNode_nodeIsEmpty_verificationSucceeds() {
        verifyEmptyNode(absenceRoot, "a:AbsenceReason");
    }

    @Test
    public void verifyEmptyNode_nodeNotFound_verificationFails() {
        try {
            verifyEmptyNode(absenceRoot, "a:unknown");
            throw new GreenbirdTestException("Expected AssertionError.");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("was null"));
        }
    }

    @Test
    public void verifyMissingNode_nodeIsMissing_verificationSucceeds() {
        verifyMissingNode(absenceRoot, "a:unknown");
    }

    @Test
    public void verifyMissingNode_nodeIsNotMissing_verificationFails() {
        try {
            verifyMissingNode(absenceRoot, "a:DateFrom");
            throw new GreenbirdTestException("Expected AssertionError.");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("Expected: is null"));
        }
    }
    
    @Test
    public void getCoverageForPrivateConstructor(){
        callPrivateDefaultConstructor(XmlTransformationTestUtil.class);
    }

}
