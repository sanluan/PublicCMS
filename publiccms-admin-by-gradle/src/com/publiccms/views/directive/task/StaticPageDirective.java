package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.FileInfo;
import com.publiccms.logic.component.FileComponent.StaticResult;
import com.sanluan.common.base.BaseTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class StaticPageDirective extends BaseTemplateDirective {
    @Autowired
    private FileComponent fileComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", "/");
        List<FileInfo> list = fileComponent.getFileList(path, true);
        List<String> messageList = new ArrayList<String>();
        messageList = deal(messageList, path, list);
        handler.put("messageList", messageList).render();
    }

    private List<String> deal(List<String> messageList, String path, List<FileInfo> list) {
        for (FileInfo fileInfo : list) {
            String filePath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                messageList = deal(messageList, filePath + "/", fileComponent.getFileList(filePath, false));
            } else {
                StaticResult result = fileComponent.staticPage(filePath);
                if (!result.getResult()) {
                    messageList.add(filePath);
                }
            }
        }
        return messageList;
    }
}
