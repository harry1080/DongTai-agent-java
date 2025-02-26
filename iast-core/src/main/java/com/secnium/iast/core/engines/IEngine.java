package com.secnium.iast.core.engines;

import com.secnium.iast.core.PropertyUtils;

import java.lang.instrument.Instrumentation;

/**
 * @author dongzhiyong@huoxian.cn
 */
public interface IEngine {
    /**
     * IAST 检测引擎初始化
     *
     * @param cfg  IAST检测引擎启动配置
     * @param inst JVM Instrumentation实例化对象
     */
    void init(PropertyUtils cfg, Instrumentation inst);

    /**
     * IAST 检测引擎启动
     */
    void start();

    /**
     * IAST 检测引擎停止
     */
    void stop();

    /**
     * IAST 检测引擎销毁
     */
    void destroy();
}
