package com.publiccms.views.method.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;

import freemarker.ext.dom.NodeModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetXmlMethod
 * 
 */
@Component
public class GetXmlMethod extends BaseMethod {
    private boolean uninitialized = true;

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String str = getString(0, arguments);
        if (CommonUtils.notEmpty(str)) {
            if (uninitialized) {
                try {
                    DocumentBuilderFactory dbf = NodeModel.getDocumentBuilderFactory();
                    dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
                    dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
                    dbf.setXIncludeAware(false);
                    dbf.setExpandEntityReferences(false);
                    uninitialized = false;
                } catch (ParserConfigurationException e) {
                }
            }
            InputSource is = new InputSource(new StringReader(str));
            try {
                return NodeModel.parse(is);
            } catch (SAXException | IOException | ParserConfigurationException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public boolean httpEnabled() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}
