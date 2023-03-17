package com.publiccms.common.document;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.fit.pdfdom.PDFDomTree;
import org.fit.pdfdom.PDFDomTreeConfig;
import org.fit.pdfdom.PathSegment;
import org.fit.pdfdom.resource.ImageResource;
import org.w3c.dom.Element;

import com.publiccms.common.tools.CommonUtils;

public class CustomPDFDomTree extends PDFDomTree {

    public CustomPDFDomTree() throws IOException {
        super();
    }

    public CustomPDFDomTree(PDFDomTreeConfig config) throws IOException {
        super(config);
    }

    @Override
    protected void createDocument() throws ParserConfigurationException {
        super.createDocument();
    }

    protected Element createPageElement() {
        Element el = super.createPageElement();
        el.removeAttribute("class");
        String style = el.getAttribute("style");
        el.setAttribute("style", CommonUtils.joinString(style, "position:relative; border:1px solid blue;margin:0.5em;"));
        return el;
    }

    protected Element createTextElement(float width) {
        Element el = super.createTextElement(width);
        el.removeAttribute("class");
        String style = el.getAttribute("style");
        el.setAttribute("style", CommonUtils.joinString(style, "position:absolute;white-space:nowrap;text-shadow:none;"));
        return el;
    }

    protected Element createRectangleElement(float x, float y, float width, float height, boolean stroke, boolean fill) {
        Element el = super.createRectangleElement(x, y, width, height, stroke, fill);
        el.removeAttribute("class");
        String style = el.getAttribute("style");
        el.setAttribute("style", CommonUtils.joinString(style, "position:absolute;"));
        return el;
    }

    protected Element createLineElement(float x1, float y1, float x2, float y2) {
        Element el = super.createLineElement(x1, y1, x2, y2);
        el.removeAttribute("class");
        String style = el.getAttribute("style");
        el.setAttribute("style", CommonUtils.joinString(style, "position:absolute;"));
        return el;
    }

    protected Element createPathImage(List<PathSegment> path) throws IOException {
        CustomPathDrawer drawer = new CustomPathDrawer(getGraphicsState());
        ImageResource renderedPath = drawer.drawPath(path);
        if (renderedPath != null) {
            return createImageElement((float) renderedPath.getX(), (float) renderedPath.getY(), renderedPath.getWidth(),
                    renderedPath.getHeight(), renderedPath);
        } else {
            return null;
        }
    }
}
