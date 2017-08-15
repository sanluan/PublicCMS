package org.publiccms.logic.service.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.publiccms.logic.dao.tools.HqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HqlService
 * 
 */
@Service
@Transactional
public class HqlService extends BaseService<Object> {

    /**
     * @param hql
     * @return
     */
    public int update(String hql) {
        return dao.update(hql);
    }

    /**
     * @param hql
     * @return
     */
    public int delete(String hql) {
        return dao.delete(hql);
    }

    /**
     * @param hql
     * @param paramters
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(String hql, Map<String, Object> paramters, Integer pageIndex, Integer pageSize) {
        return dao.getPage(hql, paramters, pageIndex, pageSize);
    }

    /**
     * @param text
     * @return
     */
    public Set<String> getToken(String text) {
        Set<String> list = new LinkedHashSet<String>();
        if (notEmpty(text)) {
            try (StringReader stringReader = new StringReader(text);
                    TokenStream tokenStream = dao.getAnalyzer().tokenStream(BLANK, stringReader)) {
                CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
                tokenStream.reset();
                while (tokenStream.incrementToken()) {
                    list.add(charTermAttribute.toString());
                }
                tokenStream.end();
                return list;
            } catch (IOException e) {
                return list;
            }
        }
        return list;
    }

    /**
     * 
     */
    public void clear() {
        dao.clear();
    }

    @Autowired
    private HqlDao dao;
    
}
