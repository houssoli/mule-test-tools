package com.greenbird.test.mule.xml;

import com.greenbird.xml.XPathRoot;
import org.mule.module.xml.transformer.XsltTransformer;
import org.springframework.util.xml.SimpleNamespaceContext;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class XmlTransformationTestUtil {
    private XmlTransformationTestUtil() {
        // NOP
    }

    public static XPathRoot transform(String source, String stylesheet, Map<String, String> namespaceMap) {
        DOMResult result;
        SimpleNamespaceContext namespaceContext;
        try {
            TransformerFactory transformerFactory = (TransformerFactory)
                    Class.forName(XsltTransformer.PREFERRED_TRANSFORMER_FACTORY).newInstance();
            Transformer transformer = transformerFactory.newTransformer(
                    new StreamSource(XmlTransformationTestUtil.class.getResourceAsStream(stylesheet)));
            result = new DOMResult();
            transformer.transform(new StreamSource(XmlTransformationTestUtil.class.getResourceAsStream(source)), result);
            namespaceContext = new SimpleNamespaceContext();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String key : namespaceMap.keySet()) {
            namespaceContext.bindNamespaceUri(key, namespaceMap.get(key));
        }
        return XPathRoot.forNode(result.getNode()).withNamespaceContext(namespaceContext);
    }

    public static void verifyCurrentDate(XPathRoot root, String path) {
        String expectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        assertThat(root.value(path), startsWith(expectedDate));
    }

    public static void verifyEmptyNode(XPathRoot absenceRoot, String path) {
        assertThat(absenceRoot.node(path), is(notNullValue()));
        assertThat(absenceRoot.value(path), is(""));
    }

    public static void verifyMissingNode(XPathRoot xPathRoot, String path) {
        assertThat(xPathRoot.node(path), is(nullValue()));
    }
}
