package com.greenbird.test.mule.xml;

import com.greenbird.test.GreenbirdTestException;
import com.greenbird.xml.XPathRoot;
import org.mule.module.xml.transformer.XsltTransformer;
import org.springframework.util.xml.SimpleNamespaceContext;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    public static TransformationBuilder transform(String source) {
        return new TransformationBuilder(source);
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

    private static XPathRoot transform(TransformationBuilder builder) {
        DOMResult result;
        SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
        try {
            TransformerFactory transformerFactory = (TransformerFactory)
                    Class.forName(XsltTransformer.PREFERRED_TRANSFORMER_FACTORY).newInstance();
            Transformer transformer = transformerFactory.newTransformer(
                    new StreamSource(XmlTransformationTestUtil.class.getResourceAsStream(builder.stylesheet)));
            for (Map.Entry<String, Object> mapEntry : builder.parameterMap.entrySet()) {
                transformer.setParameter(mapEntry.getKey(), mapEntry.getValue());
            }
            result = new DOMResult();
            transformer.transform(new StreamSource(XmlTransformationTestUtil.class.getResourceAsStream(builder.source)), result);
        } catch (Exception e) {
            throw new GreenbirdTestException(e);
        }
        for (String key : builder.namespaceMap.keySet()) {
            namespaceContext.bindNamespaceUri(key, builder.namespaceMap.get(key));
        }
        return XPathRoot.forNode(result.getNode()).withNamespaceContext(namespaceContext);
    }

    public static class TransformationBuilder {
        private String source;
        private String stylesheet;
        private Map<String, String> namespaceMap = new HashMap<String, String>();
        private Map<String, Object> parameterMap = new HashMap<String, Object>();

        public TransformationBuilder(String source) {
            this.source = source;
        }

        public TransformationBuilder withNamespace(String prefix, String namespace) {
            namespaceMap.put(prefix, namespace);
            return this;
        }

        public TransformationBuilder withParameter(String name, Object value) {
            parameterMap.put(name, value);
            return this;
        }

        public XPathRoot usingStylesheet(String stylesheet) {
            this.stylesheet = stylesheet;
            return transform(this);
        }

    }

}
