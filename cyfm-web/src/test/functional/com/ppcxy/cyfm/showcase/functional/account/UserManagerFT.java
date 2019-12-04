package com.ppcxy.cyfm.showcase.functional.account;

import com.ppcxy.cyfm.showcase.functional.BaseSeleniumTestCase;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springside.modules.test.category.Smoke;
import org.springside.modules.test.selenium.Selenium2;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 用户管理的功能测试.
 *
 * @author calvin
 */
public class UserManagerFT extends BaseSeleniumTestCase {

    @Test
    @Category(Smoke.class)
    public void list() {
        s.open("/manage");
        loginAsAdminIfNecessary();

        s.getDriver().switchTo().frame("leftFrame");
        s.click(By.linkText("用户管理"));

        s.getDriver().switchTo().defaultContent();

        s.waitForCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt("rightFrame"), 20000);

        WebElement table = s.findElement(By.id("contentTable"));
        assertThat(s.getTable(table, 5, 2)).isEqualTo("管理员 ");
    }

    @Test
    @Category(Smoke.class)
    public void editUser() {
        s.open("/manage");
        loginAsAdminIfNecessary();
        s.getDriver().switchTo().frame("leftFrame");
        s.click(By.linkText("用户管理"));

        s.getDriver().switchTo().defaultContent();

        s.waitForCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt("rightFrame"), Selenium2.DEFAULT_WAIT_TIME);
        s.click(By.id("editLink-user"));

        // 点击提交按钮
        s.type(By.name("name"), "user_foo");
        s.check(By.id("status2"));
        s.click(By.id("submit_btn"));

        // 重新进入用户修改页面, 检查最后修改者
        s.click(By.id("editLink-user"));
        assertThat(s.getValue(By.name("name"))).isEqualTo("user_foo");
        assertThat(s.isChecked(By.id("status2"))).isTrue();

        // 恢复原有值
        s.type(By.name("name"), "user");
        s.check(By.id("status1"));
        s.click(By.id("submit_btn"));
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
