package com.secnium.iast.core.enhance.plugins.framework.tomcat;

import com.secnium.iast.core.enhance.IASTContext;
import com.secnium.iast.core.enhance.plugins.AbstractClassVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;

public class ByteChunkVisitor extends AbstractClassVisitor {


    public ByteChunkVisitor(ClassVisitor classVisitor, IASTContext context) {
        super(classVisitor, context);
    }

    @Override
    public boolean hasTransformed() {
        return transformed;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        int bool = (!Modifier.isNative(access) && !Modifier.isAbstract(access)) ? 1 : 0;
        if (1 == bool && "recycle".equals(name)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Instrumenting Tomcat's ByteChunk recycle() method");
            }
            mv = new ByteChunkAdapter(mv, access, name, desc, context);
            transformed = true;
        }
        return mv;
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
}
