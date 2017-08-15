package org.publiccms.views.method.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.publiccms.common.base.BaseMethod;

import freemarker.ext.dom.NodeModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetXmlMethod
 * 
 */
@Component
public class GetXmlMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String str = getString(0, arguments);
        if (notEmpty(str)) {
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
    public int minParamtersNumber() {
        return 1;
    }
}
