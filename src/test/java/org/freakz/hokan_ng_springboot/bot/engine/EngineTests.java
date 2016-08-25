package org.freakz.hokan_ng_springboot.bot.engine;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Petri Airio on 24.4.2015.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = HokanNgSpringBootEngine.class)
public class EngineTests {

    @Test
    public void testTranslateEngFi() {
        Assert.assertTrue(true);
    }

    @Test
    public void testNimiPvmParse() {
        String nimiOrPvm = "13.10.";


        if (nimiOrPvm.matches("\\d+\\.\\d+\\.")) {
            int foo = 0;
        }


        Assert.assertTrue(true);
    }

}
