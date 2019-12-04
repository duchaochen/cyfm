$cy.maintain = {
    staticResource: {
        initBtn: function () {
            $(".btn-version").click(function () {
                $cy.waiting("正在执行...");
                var tr = $(this).closest("tr");
                var fileName = tr.find("[name='fileName']").val();
                var content = tr.find("[name='content']").val();
                var version = tr.find("[name='version']").text();
                if (!version) {
                    version = "1";
                } else {
                    version = parseInt(version) + 1;
                }

                var ajaxUrl = ctx + "/manage/maintain/staticResource/incVersion";
                $.ajax({
                    url: ajaxUrl,
                    type: "post",
                    dataType: "json",
                    data: {
                        fileName: fileName,
                        content: content,
                        newVersion: version
                    },
                    success: function (result) {
                        top.$cy.waitingOver();
                        tr.find("[name='version']").text(version);
                        tr.find("[name='content']").val(result.content);
                        $cy.info('操作成功');
                    }
                });
                return false;
            });

            $(".btn-batch-version").click(function () {
                var checkboxes = $cy.table.getAllSelectedCheckbox($("#contentTable"));
                if (!checkboxes.length) {
                    return;
                }
                $cy.confirm({
                    message: "确认选中的js/css版本+1吗？",
                    yes: function () {
                        var fileNames = [];
                        var contents = [];
                        var versions = [];
                        for (var i = 0, l = checkboxes.length; i < l; i++) {
                            var tr = $(checkboxes[i]).closest("tr");

                            var fileName = tr.find("[name='fileName']").val();
                            var content = tr.find("[name='content']").val();
                            var version = tr.find("[name='version']").text();
                            if (!version) {
                                version = "1";
                            } else {
                                version = parseInt(version) + 1;
                            }
                            fileNames[fileNames.length] = fileName;
                            contents[contents.length] = content;
                            versions[versions.length] = version;
                        }

                        var url = ctx + "/manage/maintain/staticResource/batchIncVersion";
                        $.post(
                            url,
                            {
                                "fileNames[]": fileNames,
                                "contents[]": contents,
                                "newVersions[]": versions
                            },
                            function (data) {
                                $cy.info(data.message, function () {
                                    window.location.reload();
                                });
                            }
                        );
                    }
                });
            });


            $(".btn-clear-version").click(function () {
                var checkboxes = $cy.table.getAllSelectedCheckbox($("#contentTable"));
                if (!checkboxes.length) {
                    return;
                }
                $cy.confirm({
                    message: "确认清空选中的js/css版本吗？",
                    yes: function () {
                        var fileNames = [];
                        var contents = [];
                        for (var i = 0, l = checkboxes.length; i < l; i++) {
                            var tr = $(checkboxes[i]).closest("tr");
                            var fileName = tr.find("[name='fileName']").val();
                            var content = tr.find("[name='content']").val();

                            fileNames[fileNames.length] = fileName;
                            contents[contents.length] = content;
                        }

                        var url = ctx + "/manage/maintain/staticResource/clearVersion";
                        $.post(
                            url,
                            {
                                "fileNames[]": fileNames,
                                "contents[]": contents
                            },
                            function (data) {
                                $cy.info(data.message, function () {
                                    window.location.reload();
                                });
                            }
                        );
                    }
                });
            });


            $(".btn-compress").click(function () {
                $cy.waiting("正在执行...");
                var tr = $(this).closest("tr");
                var fileName = tr.find("[name='fileName']").val();
                var content = tr.find("[name='content']").val();

                var url = ctx + "/manage/maintain/staticResource/compress";
                $.post(
                    url,
                    {
                        fileName: fileName,
                        content: content
                    }
                    ,
                    function (data) {
                        $cy.waitingOver();
                        if (data.success) {
                            $cy.info(data.message);
                        } else {
                            $cy.error(data.message);
                        }

                    }
                );
                return false;
            });


            $(".btn-batch-compress").click(function () {
                var checkboxes = $cy.table.getAllSelectedCheckbox($("#contentTable"));
                if (!checkboxes.length) {
                    return;
                }
                $cy.confirm({
                    message: "确认压缩选中的js/css吗？",
                    yes: function () {
                        var fileNames = [];
                        var contents = [];
                        for (var i = 0, l = checkboxes.length; i < l; i++) {
                            var tr = $(checkboxes[i]).closest("tr");

                            var fileName = tr.find("[name='fileName']").val();
                            var content = tr.find("[name='content']").val();
                            fileNames[fileNames.length] = fileName;
                            contents[contents.length] = content;
                        }

                        var url = ctx + "/manage/maintain/staticResource/batchCompress";
                        $.post(
                            url,
                            {
                                "fileNames[]": fileNames,
                                "contents[]": contents
                            },
                            function (data) {
                                $cy.info(data.message, function () {
                                    window.location.reload();
                                });
                            }
                        );
                    }
                });

            });

            $(".btn-switch").click(function () {
                $cy.waiting("正在执行...");
                var btn = $(this);
                var tr = btn.closest("tr");
                var fileName = tr.find("[name='fileName']").val();
                var content = tr.find("[name='content']").val();

                var url = ctx + "/manage/maintain/staticResource/switch";
                $.post(
                    url,
                    {
                        fileName: fileName,
                        content: content,
                        min: btn.hasClass("min")
                    }
                    ,
                    function (data) {
                        $cy.waitingOver();
                        if (data.success) {
                            tr.find("[name='content']").val(data.content);
                            tr.find("[name='url']").text(data.url);
                            $cy.info(data.message);
                        } else {
                            $cy.error(data.message);
                        }

                    }
                );
                return false;
            });


            $(".btn-batch-switch").click(function () {
                var btn = $(this);
                var checkboxes = $cy.table.getAllSelectedCheckbox($("#contentTable"));
                if (!checkboxes.length) {
                    return;
                }
                $cy.confirm({
                    message: "确认切换选中的js/css吗？",
                    yes: function () {
                        var fileNames = [];
                        var contents = [];
                        for (var i = 0, l = checkboxes.length; i < l; i++) {
                            var tr = $(checkboxes[i]).closest("tr");

                            var fileName = tr.find("[name='fileName']").val();
                            var content = tr.find("[name='content']").val();
                            fileNames[fileNames.length] = fileName;
                            contents[contents.length] = content;
                        }

                        var url = ctx + "/manage/maintain/staticResource/batchSwitch";
                        $.post(
                            url,
                            {
                                "fileNames[]": fileNames,
                                "contents[]": contents,
                                min: btn.hasClass("min")
                            },
                            function (data) {
                                $cy.info(data.message, function () {
                                    window.location.reload();
                                });
                            }
                        );
                    }
                });
            });
        }
    }
};
