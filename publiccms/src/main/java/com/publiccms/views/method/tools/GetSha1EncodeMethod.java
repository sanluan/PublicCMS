package com.publiccms.views.method.tools;

import static com.sanluan.common.tools.VerificationUtils.typeToHex;

import java.security.MessageDigest;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetSha1EncodeMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String str = getString(0, arguments);
        if (notEmpty(str)) {
            return SHA1Encode(str);
        }
        return str;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParamtersNumber() {
        return 1;
    }

    public static String SHA1Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = typeToHex(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }
}
