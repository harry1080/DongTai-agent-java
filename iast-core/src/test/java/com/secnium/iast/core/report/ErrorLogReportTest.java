package com.secnium.iast.core.report;

import com.secnium.iast.core.PropertyUtils;
import org.junit.Test;

public class ErrorLogReportTest {

    @Test
    public void send() throws Exception {
        PropertyUtils.getInstance("～/.iast/config/iast.properties");
        AgentRegisterReport.send();

        PropertyUtils.getInstance("～/.iast/config/iast.properties");
        ErrorLogReport.sendErrorLog("hello, i am error log");
        VulnReport report = new VulnReport(1000);
        report.send();
    }
}
