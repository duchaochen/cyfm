package com.ppcxy.cyfm.showcase.functional.ajax;

import com.ppcxy.cyfm.showcase.functional.BaseSeleniumTestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springside.modules.utils.Threads;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 测试Ajax Mashup.
 *
 * @calvin
 */
public class AjaxFT extends BaseSeleniumTestCase {

    @Test
    public void mashup() {
        s.open("/manage");
        loginAsAdminIfNecessary();

        s.getDriver().switchTo().frame("leftFrame");
        s.click(By.xpath("//a[contains(text(),'Web演示')]//ancestor::dd//div[contains(@class,'title')]"));
        Threads.sleep(800);
        s.click(By.linkText("Web演示"));

        s.getDriver().switchTo().defaultContent();
        s.waitForCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt("rightFrame"), 20000);

        s.click(By.linkText("跨域名Mashup演示"));

        s.click(By.xpath("//input[@value='获取内容']"));
        s.waitForVisible(By.id("mashupContent"));
        assertThat(s.getText(By.id("mashupContent"))).isEqualTo("你好，世界！");
    }

    private void loginAsAdminIfNecessary() {
        // 修改用户需要登录管理员权限
        if (s.getTitle().contains("登录页")) {
            s.type(By.name("username"), "admin");
            s.type(By.name("password"), "admin");
            s.click(By.id("submit_btn"));
        }
    }
}
