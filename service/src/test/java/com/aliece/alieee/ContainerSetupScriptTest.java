package com.aliece.alieee;

import com.aliece.alieee.client.AppUtil;
import com.aliece.alieee.container.startup.ContainerSetupScript;
import com.aliece.alieee.demo.AService;
import com.aliece.alieee.demo.B;
import com.aliece.alieee.domain.message.DomainMessage;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by wuyuan on 17/2/27.
 */
public class ContainerSetupScriptTest extends TestCase {

    private ContainerSetupScript css;

    protected void setUp() throws Exception {
        super.setUp();
        css = new ContainerSetupScript();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testValueEventProcessor() {
        AppUtil context = new AppUtil();
        css.initialized(context);

        css.prepare("", context);

        AService aService = (AService) context.getComponentInstance("aService");

        DomainMessage res = aService.commandA("11",new B(), 100);

        long start = System.currentTimeMillis();
        int result = 0;
        DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
        if (res1 != null && res1.getBlockEventResult() != null)
            result = (Integer) res1.getBlockEventResult();

        long stop = System.currentTimeMillis();
//        Assert.assertEquals(result, 100);
        System.out.print("\n ok \n" + result + " time:" + (stop - start));

    }

}
