UE.registerUI('progressBar', function(editor, uiName) {
    var me = editor;
    editor.commands["insertprogressbar"] = {
        execCommand: function(cmd, progressbarObj) {
            //插入代码
            var html = "<img class='mt-progressbar' src='"+contextPath+"/images/progressbar.png' tval='"+progressbarObj.value+"' data=\""+JSON.stringify(progressbarObj).replace(/\"/gi,"'")+"\" />";
            me.execCommand("inserthtml", html, true);
            var rng = this.selection.getRange();
            //图片对齐方式
            //me.execCommand('imagefloat',"center");
        },
        queryCommandState: function() {
            var img = me.selection.getRange().getClosedNode(),
                flag = img && (img.className == "mt-progressbar" || img.className.indexOf("mt-progressbar") != -1);
            return flag ? 1 : 0;
        }
    };
    //点击弹出窗口信息
    var popup = new baidu.editor.ui.Popup({
        editor: editor,
        content: '',
        className: 'edui-bubble',
        _updateIframe: function() { //弹出
            var img = editor._iframe = popup.anchorEl;
            if (img.className =='mt-progressbar' || img.className.indexOf("mt-progressbar") != -1) {
                editor.selection.getRange().selectNode(img).select();
                dialog.open();
                popup.hide();
            } else {
                //dialog.open();
               // popup.hide();
            }
        }
    });
    popup.render();

    //增加监听事件 监听是否点击了进度条
    editor.addListener('click', function(t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        if (editor.ui._dialogs.insertframeDialog && /img/ig.test(el.tagName) && (el.className == "mt-progressbar" || el.className.indexOf("mt-progressbar") != -1)) {
            var html = popup.formatHtml(
                '<nobr>' + editor.getLang("property") + ': <span class="edui-clickable">' + editor.getLang("default") 
                + '&nbsp;&nbsp;<br/><br/>' +
                ' <span onclick="$$._updateIframe( this);" class="edui-clickable">' + editor.getLang("modify") + '</span></nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });

    //创建dialog
    var dialog = new UE.ui.Dialog({
        //指定弹出层中页面的路径，这里只能支持页面,因为跟addCustomizeDialog.js相同目录，所以无需加路径
        iframeUrl:  contextPath+'/scripts/ueditor/exp/progressbar.html',
        //需要指定当前的编辑器实例
        editor: editor,
        //指定dialog的名字
        name: uiName,
        //dialog的标题
        title: "进度条配置",

        //指定dialog的外围样式
        cssRules: "width:600px;height:300px;",

        //如果给出了buttons就代表dialog有确定和取消
        buttons: [{
            className: 'edui-okbutton',
            label: '确定',
            onclick: function() {
                dialog.close(true);
            }
        }, {
            className: 'edui-cancelbutton',
            label: '取消',
            onclick: function() {
                dialog.close(false);
            }
        }]
    });

    var iconUrl = contextPath + '/images/ui/ui-progress-bar.png';
    //参考addCustomizeButton.js  返回工具条按钮
    var btn = new UE.ui.Button({
        name: '进度条',
        title: '进度条',
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules: 'background-image:url('+iconUrl+') !important;background-size: cover;',
        onclick: function() {
            //渲染dialog
            dialog.render();
            dialog.open();
        }
    });

    return btn;
} /*index 指定添加到工具栏上的那个位置，默认时追加到最后,editorId 指定这个UI是那个编辑器实例上的，默认是页面上所有的编辑器都会添加这个按钮*/ );