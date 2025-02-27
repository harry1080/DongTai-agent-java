package com.secnium.iast.core.enhance.plugins.sources.servlet;

import com.secnium.iast.core.enhance.IASTContext;
import com.secnium.iast.core.enhance.plugins.DispatchPlugin;
import org.objectweb.asm.ClassVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dongzhiyong@huoxian.cn
 */
public class DispatchServlet implements DispatchPlugin {
    private final String BASE_CLASS = " javax/servlet/ServletRequest".substring(1);
    private IASTContext context;
    private final Logger logger = LoggerFactory.getLogger(DispatchServlet.class);

    @Override
    public ClassVisitor dispatch(ClassVisitor classVisitor, IASTContext context) {
        this.context = context;
        String classname = isMatch();
        if (null != classname) {
            logger.debug("current class {} hit rule \"javax/servlet/ServletRequest\"", classname);
            classVisitor = new ServletClassVisitor(classVisitor, context);
        }
        return classVisitor;
    }

    @Override
    public String isMatch() {
        return this.context.getAncestors().contains(BASE_CLASS) ? this.context.getClassName() : null;
    }
}
