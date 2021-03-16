/*
 * JQuery zTree exHideNodes v3.5.40
 * http://treejs.cn/
 *
 * Copyright (c) 2010 Hunter.z
 *
 * Licensed same as jquery - MIT License
 * http://www.opensource.org/licenses/mit-license.php
 *
 * email: hunter.z@263.net
 * Date: 2019-01-18
 */
(function(j){j.extend(!0,j.fn.zTree._z,{view:{clearOldFirstNode:function(c,a){for(var b=a.getNextNode();b;){if(b.isFirstNode){b.isFirstNode=!1;e.setNodeLineIcos(c,b);break}if(b.isLastNode)break;b=b.getNextNode()}},clearOldLastNode:function(c,a,b){for(a=a.getPreNode();a;){if(a.isLastNode){a.isLastNode=!1;b&&e.setNodeLineIcos(c,a);break}if(a.isFirstNode)break;a=a.getPreNode()}},makeDOMNodeMainBefore:function(c,a,b){a=d.isHidden(a,b);c.push("<li ",a?"style='display:none;' ":"","id='",b.tId,"' class='",
            m.className.LEVEL,b.level,"' tabindex='0' hidefocus='true' treenode>")},showNode:function(c,a){d.isHidden(c,a,!1);d.initShowForExCheck(c,a);k(a,c).show()},showNodes:function(c,a,b){if(a&&a.length!=0){var f={},g,i;for(g=0,i=a.length;g<i;g++){var h=a[g];if(!f[h.parentTId]){var u=h.getParentNode();f[h.parentTId]=u===null?d.getRoot(c):h.getParentNode()}e.showNode(c,h,b)}for(var j in f)a=d.nodeChildren(c,f[j]),e.setFirstNodeForShow(c,a),e.setLastNodeForShow(c,a)}},hideNode:function(c,a){d.isHidden(c,a,
            !0);a.isFirstNode=!1;a.isLastNode=!1;d.initHideForExCheck(c,a);e.cancelPreSelectedNode(c,a);k(a,c).hide()},hideNodes:function(c,a,b){if(a&&a.length!=0){var f={},g,i;for(g=0,i=a.length;g<i;g++){var h=a[g];if((h.isFirstNode||h.isLastNode)&&!f[h.parentTId]){var j=h.getParentNode();f[h.parentTId]=j===null?d.getRoot(c):h.getParentNode()}e.hideNode(c,h,b)}for(var k in f)a=d.nodeChildren(c,f[k]),e.setFirstNodeForHide(c,a),e.setLastNodeForHide(c,a)}},setFirstNode:function(c,a){var b=d.nodeChildren(c,a),f=
            d.isHidden(c,b[0],!1);b.length>0&&!f?b[0].isFirstNode=!0:b.length>0&&e.setFirstNodeForHide(c,b)},setLastNode:function(c,a){var b=d.nodeChildren(c,a),f=d.isHidden(c,b[0]);b.length>0&&!f?b[b.length-1].isLastNode=!0:b.length>0&&e.setLastNodeForHide(c,b)},setFirstNodeForHide:function(c,a){var b,f,g;for(f=0,g=a.length;f<g;f++){b=a[f];if(b.isFirstNode)break;if(!d.isHidden(c,b)&&!b.isFirstNode){b.isFirstNode=!0;e.setNodeLineIcos(c,b);break}else b=null}return b},setFirstNodeForShow:function(c,a){var b,f,
            g,i,h;for(f=0,g=a.length;f<g;f++){b=a[f];var j=d.isHidden(c,b);if(!i&&!j&&b.isFirstNode){i=b;break}else if(!i&&!j&&!b.isFirstNode)b.isFirstNode=!0,i=b,e.setNodeLineIcos(c,b);else if(i&&b.isFirstNode){b.isFirstNode=!1;h=b;e.setNodeLineIcos(c,b);break}}return{"new":i,old:h}},setLastNodeForHide:function(c,a){var b,f;for(f=a.length-1;f>=0;f--){b=a[f];if(b.isLastNode)break;if(!d.isHidden(c,b)&&!b.isLastNode){b.isLastNode=!0;e.setNodeLineIcos(c,b);break}else b=null}return b},setLastNodeForShow:function(c,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  a){var b,f,g,i;for(f=a.length-1;f>=0;f--){b=a[f];var h=d.isHidden(c,b);if(!g&&!h&&b.isLastNode){g=b;break}else if(!g&&!h&&!b.isLastNode)b.isLastNode=!0,g=b,e.setNodeLineIcos(c,b);else if(g&&b.isLastNode){b.isLastNode=!1;i=b;e.setNodeLineIcos(c,b);break}}return{"new":g,old:i}}},data:{initHideForExCheck:function(c,a){if(d.isHidden(c,a)&&c.check&&c.check.enable){if(typeof a._nocheck=="undefined")a._nocheck=!!a.nocheck,a.nocheck=!0;a.check_Child_State=-1;e.repairParentChkClassWithSelf&&e.repairParentChkClassWithSelf(c,
            a)}},initShowForExCheck:function(c,a){if(!d.isHidden(c,a)&&c.check&&c.check.enable){if(typeof a._nocheck!="undefined")a.nocheck=a._nocheck,delete a._nocheck;if(e.setChkClass){var b=k(a,m.id.CHECK,c);e.setChkClass(c,b,a)}e.repairParentChkClassWithSelf&&e.repairParentChkClassWithSelf(c,a)}}}});var j=j.fn.zTree,l=j._z.tools,m=j.consts,e=j._z.view,d=j._z.data,k=l.$;d.isHidden=function(c,a,b){if(!a)return!1;c=c.data.key.isHidden;typeof b!=="undefined"?(typeof b==="string"&&(b=l.eqs(b,"true")),a[c]=!!b):
    a[c]=typeof a[c]=="string"?l.eqs(a[c],"true"):!!a[c];return a[c]};d.exSetting({data:{key:{isHidden:"isHidden"}}});d.addInitNode(function(c,a,b){a=d.isHidden(c,b);d.isHidden(c,b,a);d.initHideForExCheck(c,b)});d.addBeforeA(function(){});d.addZTreeTools(function(c,a){a.showNodes=function(a,b){e.showNodes(c,a,b)};a.showNode=function(a,b){a&&e.showNodes(c,[a],b)};a.hideNodes=function(a,b){e.hideNodes(c,a,b)};a.hideNode=function(a,b){a&&e.hideNodes(c,[a],b)};var b=a.checkNode;if(b)a.checkNode=function(f,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         e,i,h){(!f||!d.isHidden(c,f))&&b.apply(a,arguments)}});var n=d.initNode;d.initNode=function(c,a,b,f,g,i,h){var j=(f?f:d.getRoot(c))[c.data.key.children];d.tmpHideFirstNode=e.setFirstNodeForHide(c,j);d.tmpHideLastNode=e.setLastNodeForHide(c,j);h&&(e.setNodeLineIcos(c,d.tmpHideFirstNode),e.setNodeLineIcos(c,d.tmpHideLastNode));g=d.tmpHideFirstNode===b;i=d.tmpHideLastNode===b;n&&n.apply(d,arguments);h&&i&&e.clearOldLastNode(c,b,h)};var o=d.makeChkFlag;if(o)d.makeChkFlag=function(c,a){(!a||!d.isHidden(c,
    a))&&o.apply(d,arguments)};var p=d.getTreeCheckedNodes;if(p)d.getTreeCheckedNodes=function(c,a,b,f){if(a&&a.length>0){var e=a[0].getParentNode();if(e&&d.isHidden(c,e))return[]}return p.apply(d,arguments)};var q=d.getTreeChangeCheckedNodes;if(q)d.getTreeChangeCheckedNodes=function(c,a,b){if(a&&a.length>0){var e=a[0].getParentNode();if(e&&d.isHidden(c,e))return[]}return q.apply(d,arguments)};var r=e.expandCollapseSonNode;if(r)e.expandCollapseSonNode=function(c,a,b,f,g){(!a||!d.isHidden(c,a))&&r.apply(e,
    arguments)};var s=e.setSonNodeCheckBox;if(s)e.setSonNodeCheckBox=function(c,a,b,f){(!a||!d.isHidden(c,a))&&s.apply(e,arguments)};var t=e.repairParentChkClassWithSelf;if(t)e.repairParentChkClassWithSelf=function(c,a){(!a||!d.isHidden(c,a))&&t.apply(e,arguments)}})(jQuery);


/**
 * required:bootstrap,jquery,ztree
 */
;(function($,window,document,undefined){

    var tSelect = function(ele, options){
        this.$element = $(ele); //主元素
        this.defaults = {
            data:{},
            inputId:"_treeSelect_input",
            name: "parentId",
            class: "form-control",
            defaultKey: "",
            defaultValue: "",
            zTreeOnClick:function (event, treeId, treeNode) {
                $_textinput.val(treeNode.menuName);
                $_texthidden.val(treeNode.id);
            }
        };
        //将一个新的空对象做为$.extend的第一个参数，defaults和用户传递的参数对象紧随其后，
        //这样做的好处是所有值被合并到这个空对象上，保护了插件里面的默认值。
        this.settings = $.extend({}, this.defaults, options);
    };

    var zTreeObj;

    tSelect.prototype = {
        initInput: function(){
            $_element = this.$element;
            var _inputDom = '<input type="text" id="' + this.settings.inputId + '" class="' + this.settings.class + '" value="' + this.settings.defaultKey + '" />';
            var _hiddenDom = '<input type="hidden" name="' + this.settings.name + '" value="' + this.settings.defaultValue + '" />';
            $_element.append(_inputDom);
			$_element.append(_hiddenDom);

            $_textinput = $_element.find("input[type='text']");
            $_texthidden = $_element.find("input[type='hidden']");

            $_textinput.focus(function () {
                $_element.addClass("showtree");
            })

            $_textinput.keyup(function () {

                var _keywords = $.trim($_textinput.val().toUpperCase());

                if(_keywords !== ""){
                    var showNodes_array = [];

                    // 查找不符合条件的叶子节点
                    function filterFunc(node){
                        var result = false;
                        if(node.menuName.toUpperCase().indexOf(_keywords)!==-1) {
                            result = true;
                        }
                        return result
                    }

                    //获取符合条件的叶子结点
                    showNodes_array=zTreeObj.getNodesByFilter(filterFunc);
                    var showNodes_array_temp = showNodes_array.concat();

                    $.each(showNodes_array_temp,function (index,node) {
                        if(typeof(node) !== "undefined"){
                            switch (node.level){
                                case 0:
                                    judge(node,0);
                                    break;
                                case 1:
                                    judge(node,1);
                                    judge(node,0);
                                    break;
                                case 2:
                                    judge(node,1);
                                    break;
                            }
                        }
                    })

                    function judge(node,type){
                        switch (type){
                            case 0:
                                if (typeof(node.children) !== "undefined" && node.children != null) {
                                    $.each(node.children,function (index,item) {
                                        showNodes_array.push(item);
                                        judge(item,0)
                                    })
                                }
                                break;
                            case 1:
                                if (typeof(node.getParentNode()) !== "undefined" && node.getParentNode() != null) {
                                    var temp = node.getParentNode();
                                    showNodes_array.push(temp);
                                    judge(temp,1)
                                }
                                break;
                        }

                    }
					
                    //showNodes_array = unique(showNodes_array)
                    zTreeObj.hideNodes(zTreeObj.transformToArray(zTreeObj.getNodes()));
                    zTreeObj.showNodes(showNodes_array);
                    zTreeObj.expandAll(true);

                }else{
                    zTreeObj.showNodes(zTreeObj.transformToArray(zTreeObj.getNodes()));
                    zTreeObj.expandAll(false);
                }

            })
        },
        dropdownMenu: function(){
            var _treeDom = '<ul id="_treeDom" class="ztree" ></ul>';
            var _menuDom =  '<div class="st-dropdown-menu">' +
                                '<div class="treeBox">' +
                                    _treeDom +
                                '</div>' +
                            '</div>';
            $_element.append(_menuDom);

            jQuery.browser={};(function(){jQuery.browser.msie=false; jQuery.browser.version=0;if(navigator.userAgent.match(/MSIE ([0-9]+)./)){ jQuery.browser.msie=true;jQuery.browser.version=RegExp.$1;}})();

            var setting = {
                data: {
                	key: {
                		name: "menuName"
                	},
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "parentId",
                        rootPId: "-1"
                    }
                },
                callback: {
                    onClick: this.settings.zTreeOnClick
                }
            };
            zTreeObj = $.fn.zTree.init($("#_treeDom"), setting, this.settings.data);

            // 设置下拉菜单以及input点击不自动关闭
            $(document).on('click', function (e) {
                if(e.target.closest(".treeSelect") == null){
                	$_element.removeClass("showtree");
                }
            });

        },
        init: function(){
            this.initInput();
            this.dropdownMenu();
        }
    }

    $.fn.treeSelect = function(opts){
        var ts = new tSelect(this, opts); //接收两个参数，主元素 + 设置参数 this: 使用这个插件的容器  opts: 外部配置
        return ts.init();
    };

})(jQuery,window,document);