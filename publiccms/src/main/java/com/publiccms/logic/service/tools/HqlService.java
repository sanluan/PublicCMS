package com.publiccms.logic.service.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.logic.dao.tools.HqlDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HqlService extends BaseService<Object> {

    public int update(String hql) {
        return dao.update(hql);
    }

    public int delete(String hql) {
        return dao.delete(hql);
    }

    @Transactional(readOnly = true)
    public PageHandler getPage(String hql, Map<String, Object> paramters, Integer pageIndex, Integer pageSize) {
        return dao.getPage(hql, paramters, pageIndex, pageSize);
    }

    public List<String> getToken(String text) {
        List<String> list = new ArrayList<String>();
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

    public void clear() {
        dao.clear();
    }

    @Autowired
    private HqlDao dao;
}
