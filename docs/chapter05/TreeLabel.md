<div style="display: flex;">
	<img src="http://121.8.219.2:18887/logo.png" height="30" />
	<div style="margin-left: 5px; font-size: 30px; line-height: 30px; font-weight: bold;">梦想家内容管理系统</div>
</div>

----------
### Tree栏目树标签

#### 适用版本
<p>
Previous_Releases_4.1.3 + 
</p>

#### 名称
<p>
栏目树标签
</p>

#### 功能
<p>
该标签用于在页面中展示栏目树标签
</p>

#### 语法
```html?linenums
{dreamer-cms:tree root="A54547W2"}{/dreamer-cms:tree}
```

#### 文件
```java?linenums
cc.iteachyou.cms.taglib.tags.TreeTag
```

#### 参数
参数名|描述
:--:|:--
root|根节点栏目的编码（-1为顶级栏目）

#### 底层字段
无

#### 范例
```html?linenums
{dreamer-cms:tree root="A54547W2" /}
```

#### 效果
会返回栏目树html片段，样式可自行控制，如
```html?linenums
<ul class='dreamer-tree'>
    <li class='dreamer-tree-root'>
        <div class='dreamer-tree-root-div'>
            <a href='/list-1hq8501w/jiejuefangan/1/20' title='解决方案'>解决方案</a>
            <ul class='dreamer-tree-children'>
                <li class='dreamer-tree-node'>
                    <div class='dreamer-tree-node-div'>
                        <a href='/cover-ghx3glm1/zhengfu' title='政府'>政府</a>
                        <ul class='dreamer-tree-children'>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-053xm0pt/tuidongjingjizengzhanghefazhan' title='推动经济增长和发展'>推动经济增长和发展</a></div>
                            </li>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-6jxfu5dd/jianshekechixufazhanweilai' title='建设可持续发展未来'>建设可持续发展未来</a></div>
                            </li>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-tj129c0d/tigonganquankekaodefuwu' title='提供安全可靠的服务'>提供安全可靠的服务</a></div>
                            </li>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-f53449nw/shixiangexinghuashuzitiyan' title='实现个性化数字体验'>实现个性化数字体验</a></div>
                            </li>
                        </ul>
                    </div>
                </li>
                <li class='dreamer-tree-node'>
                    <div class='dreamer-tree-node-div'><a href='/cover-051fu5ij/xiehui' title='协会'>协会</a>
                        <ul class='dreamer-tree-children'>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-t6vvi4a9/liuchengfansuofuza' title='流程繁琐复杂'>流程繁琐复杂</a></div>
                            </li>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-65gl4ltz/hudongchangjingdanyi' title='互动场景单一'>互动场景单一</a></div>
                            </li>
                        </ul>
                    </div>
                </li>
                <li class='dreamer-tree-node'>
                    <div class='dreamer-tree-node-div'><a href='/cover-0n629d19/qiye' title='企业'>企业</a>
                        <ul class='dreamer-tree-children'>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-rx9e963g/jiakuaininchenggongbufa' title='加快您成功步伐'>加快您成功步伐</a></div>
                            </li>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-14a3rjst/jianhuaanquanbaohu' title='简化安全保护'>简化安全保护</a></div>
                            </li>
                            <li class='dreamer-tree-node'>
                                <div class='dreamer-tree-node-div'><a href='/cover-nky008yg/baohunindeweilai' title='保护您的未来'>保护您的未来</a></div>
                            </li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </li>
</ul>
```
