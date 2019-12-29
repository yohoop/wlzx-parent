package net.wanho.manage_cms;

import net.wano.po.cms.CmsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRestTemplate(){
        CmsConfig cmsConfig = restTemplate.getForObject("http://localhost:31001/cms/config/getmodel/1", CmsConfig.class);
        System.out.println(cmsConfig);
    }
}
