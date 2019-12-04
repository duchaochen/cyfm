INSERT INTO cy_sys_user (id, username, name, email,tel, password, salt, status, team_id,deleted,create_date) VALUES(1,'admin','管理员','admin@springside.org.cn','13888888888','691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','enabled',1,0,'2012-01-01 00:00:00');
INSERT INTO cy_sys_user (id, username, name, email,tel, password, salt, status, team_id,deleted,create_date) VALUES(2,'user','Calvin','user@springside.org.cn','13211111112','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled',1,0,'2012-01-01 00:00:00');
INSERT INTO cy_sys_user (id, username, name, email,tel, password, salt, status, team_id,deleted,create_date)VALUES(3,'user2','Jack','jack@springside.org.cn','13211111113','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled',1,0,'2012-01-01 00:00:00');
INSERT INTO cy_sys_user (id, username, name, email,tel, password, salt, status, team_id,deleted,create_date) VALUES(4,'user3','Kate','kate@springside.org.cn','13211111114','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled',1,0,'2012-01-01 00:00:00');
INSERT INTO cy_sys_user (id, username, name, email,tel, password, salt, status, team_id,deleted,create_date) VALUES(5,'user4','Sawyer','sawyer@springside.org.cn','13211111115','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled',1,0,'2012-01-01 00:00:00');
INSERT INTO cy_sys_user (id, username, name, email,tel, password, salt, status, team_id,deleted,create_date) VALUES(6,'user5','Ben','ben@springside.org.cn','13211111116','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled',1,0,'2012-01-01 00:00:00');


INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (1, '系统功能', 'fa fa-sun-o', 'root', 0, '0', '/manage/index/', 1, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (2, '我的工作台', 'fa fa-cubes', 'root', 0, '0', '/manage/index/', 2, true, 0);


INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (20, '系统管理', 'fa fa-cogs', 'sys', 1, '01/', '', 1, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (21, '用户管理', 'fa fa-user', 'sys:user', 20, '01/20/', '/sys/user', 1, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (22, '团队管理', 'fa fa-group', 'sys:team', 20, '01/20/', '/sys/team', 2, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (23, '角色管理', 'fa fa-credit-card', 'sys:role', 20, '01/20/', '/sys/role', 3, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (24, '权限管理', 'fa fa-key', 'sys:permission', 20, '01/20/', '/sys/permission', 4, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (25, '授权管理', 'fa fa-unlock-alt', 'sys:authorize', 20, '01/20/', '/sys/authorize', 5, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (26, '资源管理', 'fa fa-list', 'sys:resource', 20, '01/20/', '/sys/resource', 6, true, 0);

INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (30, '系统监控', 'fa  fa-dashboard', 'monitor', 1, '01/', '', 2, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (31, 'Druid监控', 'fa fa-database', 'monitor:druid', 30, '01/30/', '/manage/monitor/druid/index.html', 1, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (32, 'JVM监控', 'fa  fa-desktop', 'monitor:jvm', 30, '01/30/', '/manage/monitor/jvm', 2, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (33, 'SQL/JPQL操作', 'fa fa-file', 'monitor:ql', 30, '01/30/', '/manage/monitor/db/sql', 3, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (34, 'Ehcache监控', 'fa fa-cloud', 'monitor:ehcache', 30, '01/30/', '/manage/monitor/ehcache', 4, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (35, 'Hibernate监控', 'fa fa-ge', 'monitor:hibernate', 30, '01/30/', '/manage/monitor/hibernate', 5, true, 0);

INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (40, '系统维护', 'fa fa-medkit', '', 1, '01/', 'maintain', 3, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (41, '静态资源管理', 'fa fa-folder-open', 'maintain:staticResource', 40, '01/40/', '/manage/maintain/staticResource', 1, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (42, '任务调度管理', 'fa fa-folder-open', 'maintain:dynamicTask', 40, '01/40/', '/manage/maintain/dynamicTask', 2, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (43, '动态数据源管理', 'fa fa-folder-open', 'maintain:datasource', 40, '01/40/', '/manage/maintain/datasource', 3, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (44, '通知模板管理', 'fa fa-folder-open', 'maintain:notificationTemplate', 40, '01/40/', '/manage/maintain/notificationTemplate', 4, true, 0);


INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (50, '演示功能', 'fa fa-film', '', 1, '01/', 'demo', 4, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (51, 'dialog演示', 'fa fa-pied-piper-alt', '', 50, '01/50/', '/demo/ui/dialog', 1, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (52, 'Web演示', 'fa fa-folder-open', '', 50, '01/50/', '/story/web', 2, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (53, 'JMX演示', 'fa fa-folder-open', '', 50, '01/50/', '/story/jmx', 3, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (54, '日志高级演示', 'fa fa-folder-open', '', 50, '01/50/', '/story/log', 4, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (55, '性能监控演示', 'fa fa-folder-open', '', 50, '01/50/', '/story/monitor', 5, true, 0);


INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (70, '远程API调用', 'fa fa-superpowers', '', 1, '01/', '', 6, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (71, '接入系统配置', 'fa fa-folder-open', 'bus:system:joinSystem', 70, '01/1006/', '/bus/system/joinSystem', 1, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (72, '接入API配置', 'fa fa-folder-open', 'bus:system:remoteApi', 70, '01/1006/', '/bus/system/remoteApi', 2, true, 0);
INSERT INTO cy_sys_resource (id, name, icon, `_identity`, parent_id, parent_ids, url, weight, is_show, resource_type) VALUES (73, '接入API调试', 'fa fa-folder-open', 'bus:system:remoteApi', 70, '01/1006/', '/bus/system/remoteApi/test', 3, true, 0);


INSERT INTO cy_maintain_notification_template (id, NAME, SYSTEM, TITLE, TEMPLATE, DELETED) VALUES (1, 'changeUser', 'system', '用户{user}信息变更.', '用户{user}信息发生变更,变更信息为:{userInfo}', 0);
INSERT INTO cy_maintain_notification_template (id, NAME, SYSTEM, TITLE, TEMPLATE, DELETED) VALUES (2, 'collectTaskExecute', 'system', '采集任务执行[{state}]通知', '采集任务执行完成, 任务实例 [{taskId}], 开始时间: {beginDate}, 结束时间: {endDate}, 总耗时: {totalTime}. 采集记录总数: {total}', 0);
INSERT INTO cy_maintain_notification_template (id, NAME, SYSTEM, TITLE, TEMPLATE, DELETED) VALUES (3, 'commonTemplate', 'system', '{title}', '{message}', 0);
INSERT INTO cy_maintain_notification_template (id, NAME, SYSTEM, TITLE, TEMPLATE, DELETED) VALUES (4, 'excelExportError', 'excel', '导出Excel失败', '导出Excel失败了，请把错误报告给管理员，可能的失败原因：文件格式不对；错误码：{error}', 0);
INSERT INTO cy_maintain_notification_template (id, NAME, SYSTEM, TITLE, TEMPLATE, DELETED) VALUES (5, 'excelExportSuccess', 'excel', '导出Excel成功', '[{model}]导出Excel成功，耗时{seconds}秒钟，<a href="{ctx}/{url}" target="_blank">点击下载</a>（注意：导出的文件只保留3天，请尽快下载，过期将删除）', 0);
INSERT INTO cy_maintain_notification_template (id, NAME, SYSTEM, TITLE, TEMPLATE, DELETED) VALUES (6, 'excelImportSuccess', 'system', '导入Excel成功', '导入Excel成功，耗时{seconds}秒钟，<a onclick="$($.find(\\''#menu a:contains(Excel导入/导出)\\'')).click();;$(\\''.notification-list .close-notification-list\\'').click();;">点击前往查看</a>', 0);
INSERT INTO cy_maintain_notification_template (id, NAME, SYSTEM, TITLE, TEMPLATE, DELETED) VALUES (7, 'excelImportError', 'system', '导入Excel失败', '导入Excel失败了，请把错误报告给管理员，可能的失败原因：文件格式不对；错误码：{error}', 0);


INSERT INTO cy_sys_role (id, name,value,description, permissions) VALUES(1,'超级管理员','Admin','超级管理员角色','*');
INSERT INTO cy_sys_role (id, name,value,description, permissions) VALUES(2,'普通用户','User','普通用户角色','user:view');


INSERT INTO cy_sys_user_role (user_id, role_id) VALUES(1,1);
INSERT INTO cy_sys_user_role (user_id, role_id) VALUES(1,2);
INSERT INTO cy_sys_user_role (user_id, role_id) VALUES(2,2);
INSERT INTO cy_sys_user_role (user_id, role_id) VALUES(3,2);
INSERT INTO cy_sys_user_role (user_id, role_id) VALUES(4,2);
INSERT INTO cy_sys_user_role (user_id, role_id) VALUES(5,2);
INSERT INTO cy_sys_user_role (user_id, role_id) VALUES(6,2);


INSERT INTO cy_sys_team (id, name, master_id) VALUES(1,'Dolphin',1);


INSERT INTO cy_sys_permission (id, `name`, `value`, description) VALUES (1, '创建权限', 'create', '资源的创建权限----值为:create 实际用法 资源标识:create');
INSERT INTO cy_sys_permission (id, `name`, `value`, description) VALUES (2, '修改权限', 'update', '资源的创建权限----值为:update 实际用法 资源标识:update ');
INSERT INTO cy_sys_permission (id, `name`, `value`, description) VALUES (3, '删除权限', 'delete', '资源的删除权限----值为:delete 实际用法 资源标识:delete ');
INSERT INTO cy_sys_permission (id, `name`, `value`, description) VALUES (4, '查看权限', 'view', '资源的查看权限----值为:view 实际用法 资源标识:view');
INSERT INTO cy_sys_permission (id, `name`, `value`, description) VALUES (5, '导入权限', 'import', '资源的导入权限----值为:import 实际用法 资源标识:import ');
INSERT INTO cy_sys_permission (id, `name`, `value`, description) VALUES (6, '导出权限', 'export', '资源的导出权限----值为:export 实际用法 资源标识:export ');
