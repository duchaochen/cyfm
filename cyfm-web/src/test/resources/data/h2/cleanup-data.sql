DELETE FROM  cy_sys_user_role;

DELETE FROM  cy_sys_role;

DELETE FROM  cy_sys_user;

DELETE FROM  cy_sys_team;

DELETE FROM  cy_sys_resource;

--清除登录状态会造成测试中断
--DELETE FROM CY_SYS_USER_ONLINE;
