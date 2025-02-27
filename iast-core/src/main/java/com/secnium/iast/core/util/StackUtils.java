package com.secnium.iast.core.util;

public class StackUtils {
    public static StackTraceElement[] createCallStack(int stackStartPos) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement[] selfCallStack = new StackTraceElement[stackTraceElements.length - stackStartPos];
        if (stackTraceElements.length - stackStartPos >= 0) {
            System.arraycopy(stackTraceElements, stackStartPos, selfCallStack, 0, stackTraceElements.length - stackStartPos);
        }
        return selfCallStack;
    }

    public static StackTraceElement getLatestStack(int stackStartPos) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[stackStartPos];
    }
}
