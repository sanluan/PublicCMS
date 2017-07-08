-- ----------------------------
-- Records of cms_category
-- ----------------------------
INSERT INTO `cms_category` VALUES ('1', '1', '演示', null, null, '17,15,12,9,8,7,6,18', '', 'demonstrate', '/category/parent.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/demonstrate/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('6', '1', '汽车', '1', null, null, '', 'car', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/car/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('7', '1', '社会', '1', null, null, '', 'social', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/social/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('8', '1', '美图', '1', null, null, '', 'picture', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/picture/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('9', '1', '系统介绍', '1', null, null, '', 'introduction', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/introduction/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', '2', null);
INSERT INTO `cms_category` VALUES ('12', '1', '文章', '1', null, null, '', 'article', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/article/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('11', '1', '测试', null, null, null, null, 'test', '/category/parent.html', '${category.code}/index.html', '0', '0', 'test/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('13', '1', '下载', null, null, null, null, 'download', '', 'https://github.com/sanluan/PublicCMS', '0',  '0', 'https://github.com/sanluan/PublicCMS', '', '1', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('14', '1', '图书', '1', null, null, null, 'book', '/category/parent.html', 'demonstrate/${category.code}/index.html', '0', '0', 'demonstrate/book/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('15', '1', '小说', '1', null, null, '', 'novel', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/novel/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('16', '1', 'OSChina下载', '13', null, null, null, 'download', '', 'http://git.oschina.net/sanluan/PublicCMS', '0', '0', 'http://git.oschina.net/sanluan/PublicCMS', '', '1', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('17', '1', '科技', '1', null, null, '', 'science', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/science/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('18', '1', '商品', '1', null, null, '', 'product', '/category/product_list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/product/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', '-3', null);
INSERT INTO `cms_category` VALUES ('19', '1', '案例', null, null, null, '', 'case', '/category/parent.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/case/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', '2', '2');
INSERT INTO `cms_category` VALUES ('20', '2', '热门楼盘', NULL, NULL, NULL, '', 'houses', NULL, 'category.html?id=${category.id}', '0', '0', '//hainan.publiccms.com/category.html?id=34', 'content.html?id=${content.id}', '1', '12', '0', '0', '0', '0', '3', null);

-- ----------------------------
-- Records of cms_category_attribute
-- ----------------------------
INSERT INTO `cms_category_attribute` VALUES ('3', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('1', '演示', 'PublicCMS,如何使用', 'PublicCMS如何使用', null);
INSERT INTO `cms_category_attribute` VALUES ('2', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('4', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('5', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('6', '汽车 - PublicCMS', '汽车,car', '汽车', null);
INSERT INTO `cms_category_attribute` VALUES ('7', '社会', '社会', '社会', null);
INSERT INTO `cms_category_attribute` VALUES ('8', '美图', '美图,美女', '美图美女', null);
INSERT INTO `cms_category_attribute` VALUES ('9', '系统介绍', 'PublicCMS,系统介绍', 'PublicCMS系统介绍', null);
INSERT INTO `cms_category_attribute` VALUES ('10', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('11', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('12', '文章', '文章', '文章', null);
INSERT INTO `cms_category_attribute` VALUES ('13', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('14', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('15', '小说', '小说,在线阅读', '小说,在线阅读', null);
INSERT INTO `cms_category_attribute` VALUES ('16', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('17', '科技', '科技', '科技频道', null);
INSERT INTO `cms_category_attribute` VALUES ('18', '商品', '商品,导购', '商品', null);
INSERT INTO `cms_category_attribute` VALUES ('19', '案例', 'PublicCMS案例', 'PublicCMS案例', null);
INSERT INTO `cms_category_attribute` VALUES ('20', '热门推荐楼盘', '海南,楼盘', '居住？投资？其实很多时候它们是同一种思路，适合居住的地方一定有投资前景，而适合投资之处也一定有适合它的居住人群。', NULL);

-- ----------------------------
-- Records of cms_category_model
-- ----------------------------
INSERT INTO `cms_category_model` VALUES ('9', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('8', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('7', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('7', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('6', '2', '');
INSERT INTO `cms_category_model` VALUES ('12', '2', '');
INSERT INTO `cms_category_model` VALUES ('12', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('15', '4', '/system/book.html');
INSERT INTO `cms_category_model` VALUES ('15', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('15', '5', '');
INSERT INTO `cms_category_model` VALUES ('9', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('9', '5', '');
INSERT INTO `cms_category_model` VALUES ('9', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('9', '2', '');
INSERT INTO `cms_category_model` VALUES ('16', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('16', '5', '');
INSERT INTO `cms_category_model` VALUES ('6', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('6', '5', '');
INSERT INTO `cms_category_model` VALUES ('6', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('6', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('8', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('8', '5', '');
INSERT INTO `cms_category_model` VALUES ('7', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('7', '5', '');
INSERT INTO `cms_category_model` VALUES ('17', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('17', '5', '');
INSERT INTO `cms_category_model` VALUES ('17', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('17', '2', '');
INSERT INTO `cms_category_model` VALUES ('17', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('7', '2', '');
INSERT INTO `cms_category_model` VALUES ('14', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('14', '5', '');
INSERT INTO `cms_category_model` VALUES ('12', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('12', '5', '');
INSERT INTO `cms_category_model` VALUES ('1', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('1', '5', '');
INSERT INTO `cms_category_model` VALUES ('18', '8', '');
INSERT INTO `cms_category_model` VALUES ('18', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('18', '5', '');
INSERT INTO `cms_category_model` VALUES ('19', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('19', '5', '');
INSERT INTO `cms_category_model` VALUES ('19', '2', '');
INSERT INTO `cms_category_model` VALUES ('18', '7', '');
INSERT INTO `cms_category_model` VALUES ('20', 'article', NULL);

-- ----------------------------
-- Records of cms_content
-- ----------------------------
INSERT INTO `cms_content` VALUES ('1', '1', 'PublicCMS合作伙伴招募', '1', '1', '9', '1', null, '0', '', '', '0', '0', '0', '1', '//www.publiccms.com/introduction/2015/11-10/194.html', 'Public CMS V1.0 8月6号预发布，10月9号发布第一份文档，已经积累了超出作者预期的用户数量。作为技能比较单一的技术人员，我一个人开发的Public CMS有着各种局限性，因此诚邀各位加入。', '1', 'upload/2017/01-15/12-05-5404301838588841.jpg', '0', '0', '0', '808', '2015-11-10 12:05:58', '2015-11-10 12:05:58', '0', '1', '0');
INSERT INTO `cms_content` VALUES ('2', '1', 'PublicCMS 2016新版本即将发布', '1', '1', '9', '1', null, '0', '', '', '0', '0', '0', '1', '//www.publiccms.com/introduction/2016/03-21/215.html', '经过三个多月的研发，PublicCMS 2016即将发布。现在已经进入内测阶段，诚邀技术人员加入到测试与新版体验中。', '1', 'upload/2017/01-15/10-39-540052697476660.png', '0', '0', '0', '250', '2016-03-21 22:47:31', '2016-03-09 10:39:56', '1', '0');
INSERT INTO `cms_content` VALUES ('3', '2', '广物海南之心', '3', '3', '20', 'article', NULL, '0', NULL, '', '0', '0', '0', '0', 'http://site2.dev.publiccms.com:8080/publiccms/content.html?id=3', '广物海南之心位于海口市新埠岛最北端，是东海岸、西海岸艺妓南渡江的交汇处，距离海口市中心城区10分钟。项目内规划有1600亩水系，有30座风格迥异的桥梁贯穿58个形状各异的岛屿。', '', 'upload/2017/02-25/08-47-260355-531728337.png', '0', '0', '0', '0', '2017-02-14 13:49:53', '2017-02-14 13:45:30', '0', '1', '0');
INSERT INTO `cms_content` VALUES ('4', '2', '雅乐居•清水湾', '3', '3', '20', 'article', NULL, '0', NULL, '', '0', '0', '0', '0', 'http://site2.dev.publiccms.com:8080/publiccms/content.html?id=4', '雅乐居清水湾携手摩根士丹利斥资300亿，精耕6载打造1.5万亩滨海度假王国，如今沿16公里景观大道、12公里海岸线已兴建成星级酒店、滨海高尔夫、780席游艇码头、国际学校等配套，被誉中国旅游地产标杆。', '', 'upload/2017/02-25/08-46-340506601999827.png', '0', '0', '0', '0', '2017-02-14 13:50:00', '2017-02-14 13:46:00', '0', '1', '0');
INSERT INTO `cms_content` VALUES ('5', '2', '置地东方广场', '3', '3', '20', 'article', NULL, '0', NULL, '', '0', '0', '0', '0', 'http://site2.dev.publiccms.com:8080/publiccms/content.html?id=5', '世贸国贸双核心商圈绝佳的黄金地段，总建筑面11万平方米，占地16亩，绿化率高达40%以上，项目758个车位，群楼高度23.9米，建筑高度260米，地上48层地下3层。', '', 'upload/2017/02-25/08-48-030837-298550298.png', '0', '0', '0', '0', '2017-02-14 13:46:24', '2017-02-14 13:46:24', '0', '1', '0');

-- ----------------------------
-- Records of cms_content_attribute
-- ----------------------------
INSERT INTO `cms_content_attribute` VALUES ('1', '', '', '{}', '<p style=\"text-indent: 2em;\">Public CMS V1.0 8月6号预发布<span style=\"text-indent: 32px;\">，</span>10月9号发布第一份文档<span style=\"text-indent: 32px;\">，</span>已经积累了超出作者预期的用户数量。作为技能比较单一的技术人员，我一个人开发的Public CMS有着各种局限性，因此诚邀各位加入<span style=\"text-indent: 32px;\">，</span>共同维护这一产品并制定今后的发展方向等。</p><p style=\"text-indent: 2em;\">Public CMS的QQ群目前已经有了70人<span style=\"text-indent: 32px;\">，</span>群号：191381542。偶尔在其他技术类的QQ群竟能遇到Public CMS的用户，让我欣喜不已，同时也知道原来除了Public CMS的交流群中的群友，PublicCMS还有很多没加群的用户。为了更好的交流，大家可以加群。</p><p style=\"text-indent: 2em;\">以下是我一些初步的想法：</p><h3 style=\"text-indent: 2em;\">技术方向<br/></h3><p>&nbsp; &nbsp; 短期（一年左右）内Public CMS的大致发展方向主要集中在功能完善上，包括：</p><ul class=\" list-paddingleft-2\" style=\"list-style-type: disc;\"><li><p style=\"text-indent: 2em;\">后台UI：功能加强，浏览器兼容性完善。或者寻找其他更完善的UI替换掉现有的dwz。</p></li><li><p style=\"text-indent: 2em;\">后台功能：内容维护扩展；页面元数据扩展；分类等排序；推荐位类型扩展；推荐位可选数据类型扩充；模板在线开发功能完善；统计；附件管理等。</p></li><li><p style=\"text-indent: 2em;\">前台模板:前台模板丰富性，美观度提升。</p></li><li><p style=\"text-indent: 2em;\">纯动态站点屏蔽静态化方面配置方面的完善。<br/></p></li></ul><p>&nbsp; &nbsp; 长期规划：多站点，集群，云端内容共享，模板定制平台，二次开发代码在线定制生成等</p><h3 style=\"text-indent: 2em;\">文档方面</h3><ul class=\" list-paddingleft-2\" style=\"list-style-type: disc;\"><li><p style=\"text-indent: 2em;\">在现有文档基础上完善操作步骤细节，二次开发部分完善。</p></li><li><p style=\"text-indent: 2em;\">以Public CMS为基础产品，结合其他产品完成满足不同业务场景的解决方案级文档。</p></li><li><p style=\"text-indent: 2em;\">Public CMS相关的第三放产品的使用、配置、二次开发手册。</p></li><li><p style=\"text-indent: 2em;\">Public CMS产品使用过程中的问题库建设。</p></li><li><p style=\"text-indent: 2em;\">开发或者使用其他BBS架设社区。</p></li></ul><h3 style=\"text-indent: 2em;\">商务方面</h3><p style=\"text-indent: 2em;\">纯公益的行为是不能长久的。Public CMS本身将永久免费开源，不收取任何授权费用，允许用户自由修改开发。在此原则下，可以在模板定制，功能定制，项目承接，技术培训，产品使用培训，或开发商业版产品等方式尝试创收。</p>', '1717');
INSERT INTO `cms_content_attribute` VALUES ('2', '', '', '{}', '<p style=\"text-indent: 2em;\">经过三个多月的研发，PublicCMS 2016即将发布。现在已经进入内测阶段，诚邀技术人员加入到测试与新版体验中。</p><p>&nbsp;&nbsp;&nbsp;&nbsp;需要注意的是现在的版本并不是稳定版，请不要使用在正式项目中。<br/></p><p>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://git.oschina.net/sanluan/PublicCMS-preview\" _src=\"http://git.oschina.net/sanluan/PublicCMS-preview\">http://git.oschina.net/sanluan/PublicCMS-preview</a></p>', '355');
INSERT INTO `cms_content_attribute` VALUES ('3', NULL, NULL, '{\"price\":\"均价：14000元/平米\",\"place\":\"海口市\"}', '<p style=\"text-align: center;\"><img src=\"http://site2.dev.publiccms.com:8080/publiccms/webfile/upload/2017/02-25/08-47-260355-531728337.png\"/></p><p>广物海南之心位于海口市新埠岛最北端，是东海岸、西海岸艺妓南渡江的交汇处，距离海口市中心城区10分钟。项目内规划有1600亩水系，有30座风格迥异的桥梁贯穿58个形状各异的岛屿。</p>', '88');
INSERT INTO `cms_content_attribute` VALUES ('4', NULL, NULL, '{\"price\":\"均价：20000元/平米\",\"place\":\"陵水县\"}', '<p style=\"text-align: center;\"><img src=\"http://site2.dev.publiccms.com:8080/publiccms/webfile/upload/2017/02-25/08-46-340506601999827.png\"/></p><p>雅乐居清水湾携手摩根士丹利斥资300亿，精耕6载打造1.5万亩滨海度假王国，如今沿16公里景观大道、12公里海岸线已兴建成星级酒店、滨海高尔夫、780席游艇码头、国际学校等配套，被誉中国旅游地产标杆。</p>', '100');
INSERT INTO `cms_content_attribute` VALUES ('5', NULL, NULL, '{\"price\":\"均价：20000元/平米\",\"place\":\"海口市\"}', '<p style=\"text-align: center;\"><img src=\"http://site2.dev.publiccms.com:8080/publiccms/webfile/upload/2017/02-25/08-48-030837-298550298.png\"/>&nbsp; &nbsp;&nbsp;<br/></p><p>世贸国贸双核心商圈绝佳的黄金地段，总建筑面11万平方米，占地16亩，绿化率高达40%以上，项目758个车位，群楼高度23.9米，建筑高度260米，地上48层地下3层。</p>', '102');

-- ----------------------------
-- Records of cms_place
-- ----------------------------
INSERT INTO `cms_place` VALUES ('1', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '142', 'PublicCMS后台截图', '//www.publiccms.com/introduction/2015/08-11/142.html', 'upload/2017/01-15/17-35-240834-18490682.jpg', '2016-03-21 21:25:19', '2016-03-21 21:24:54', '1', '6', '0');
INSERT INTO `cms_place` VALUES ('2', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '159', '美食', '//www.publiccms.com/picture/2015/08-13/159.html', 'upload/2017/01-15/17-35-150887-240130090.jpg', '2016-03-21 21:26:26', '2016-03-21 21:26:08', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('3', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '9', '昂科拉', '//www.publiccms.com/car/2015/08-06/9.html', 'upload/2017/01-15/17-35-0606061972977756.jpg', '2016-03-21 21:28:57', '2016-03-21 21:28:36', '1', '8', '0');
INSERT INTO `cms_place` VALUES ('4', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '179', 'PublicCMS系统使用手册下载', '//www.publiccms.com/introduction/2015/10-09/179.html', 'upload/2017/01-15/17-34-560426-203327271.jpg', '2016-03-21 21:30:25', '2016-03-21 21:43:45', '1', '18', '0');
INSERT INTO `cms_place` VALUES ('5', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '195', '我们的婚纱照', '//www.publiccms.com/picture/2015/11-15/195.html', 'upload/2017/01-15/17-34-450591-326203189.jpg', '2016-03-21 21:31:04', '2016-03-20 21:30:46', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('6', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'Public CMS QQ群', 'http://shang.qq.com/wpa/qunwpa?idkey=8a633f84fb2475068182d3c447319977faca6a14dc3acf8017a160d65962a175', '', '2016-03-21 22:10:33', '2016-03-21 22:10:26', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('7', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'FreeMarker语法在线测试', 'http://sanluan.com/freemarker_test.html', '', '2016-03-21 22:11:57', '2016-03-21 22:11:47', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('8', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, '百度搜索：PublicCMS', 'https://www.baidu.com/s?wd=publiccms', '', '2016-03-21 22:12:12', '2016-03-21 22:12:00', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('9', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'FreeMarker2.3.23中文手册', 'http://www.kerneler.com/freemarker2.3.23/', '', '2016-03-21 22:12:24', '2016-03-21 22:12:14', '1', '5', '0');
INSERT INTO `cms_place` VALUES ('10', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'FreeMarker2.3.23视频教程', 'http://www.elsyy.com/course/6841', '', '2016-03-21 22:12:51', '2016-03-21 22:12:37', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('11', '1', '/index.html/5cf1b463-8d14-4ba4-a904-890ec224dc99.html', '1', 'custom', null, '管理后台', '//cms.publiccms.com/admin/', '', '2016-03-21 22:13:54', '2016-03-21 22:13:49', '1', '0', '1');
INSERT INTO `cms_place` VALUES ('12', '1', '/index.html/5cf1b463-8d14-4ba4-a904-890ec224dc99.html', '1', 'custom', null, '后台UI', '//image.publiccms.com/ui/', '', '2016-03-21 22:14:06', '2016-03-21 22:13:56', '1', '22', '0');
INSERT INTO `cms_place` VALUES ('13', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, '洪越源码', 'http://www.softhy.net/soft/36775.htm', '', '2016-03-23 11:03:50', '2016-03-23 11:03:31', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('14', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, 'ASP300源码', 'http://www.asp300.com/SoftView/13/SoftView_59265.html', '', '2016-03-23 11:04:10', '2016-03-23 11:03:53', '1', '2', '0');
INSERT INTO `cms_place` VALUES ('15', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, '脚本之家', 'http://www.jb51.net/codes/389534.html', '', '2016-03-23 11:04:24', '2016-03-23 11:04:11', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('16', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, '站长之家下载', 'http://down.chinaz.com/soft/37488.htm', '', '2016-03-23 11:04:42', '2016-03-23 11:04:33', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('17', '1', '/index.html/d1bef19f-ec32-4c3b-90f9-b25ca0fe19e3.html', '1', 'custom', null, '成品网站模板超市', 'http://demo.edge-cloud.cn/', '', '2016-03-23 11:12:03', '2016-03-23 11:12:32', '1', '6', '1');
INSERT INTO `cms_place` VALUES ('18', '1', '/index.html/d1bef19f-ec32-4c3b-90f9-b25ca0fe19e3.html', '1', 'custom', null, 'QQ联系作者', 'http://wpa.qq.com/msgrd?v=3&uin=315415433&site=qq&menu=yes', '', '2016-03-23 11:12:23', '2016-03-23 11:12:05', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('19', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS@Github', 'https://github.com/sanluan/PublicCMS.', '', '2016-03-23 11:13:33', '2016-03-23 11:13:25', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('20', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS@开源中国', 'http://git.oschina.net/sanluan/PublicCMS', '', '2016-03-23 11:13:48', '2016-03-23 11:13:35', '1', '5', '0');
INSERT INTO `cms_place` VALUES ('21', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS@CSDN', 'https://code.csdn.net/zyyy358/publiccms', '', '2016-03-23 11:14:03', '2016-03-23 11:13:50', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('22', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS-preview@开源中国', 'http://git.oschina.net/sanluan/PublicCMS-preview', '', '2016-03-23 11:14:30', '2016-03-23 11:14:09', '1', '5', '0');
INSERT INTO `cms_place` VALUES ('23', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, '搞机哥-博客', 'http://www.gaojig.com/', '', '2016-03-23 11:15:16', '2016-03-23 11:15:07', '1', '14', '0');
INSERT INTO `cms_place` VALUES ('24', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, '锋云科技', 'http://www.edge-cloud.cn/', '', '2016-03-23 11:15:28', '2016-03-23 11:15:21', '1', '20', '0');
INSERT INTO `cms_place` VALUES ('25', '1', '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', '1', 'content', '4', '唯美动漫图片', '//www.publiccms.com/8/4.html', '2015/08/07/11-24-1308292097994334.jpg', '2016-03-23 11:22:57', '2016-03-23 11:22:04', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('26', '1', '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', '1', 'content', '9', '昂科拉', '//www.publiccms.com/6/9.html', '2015/08/07/11-24-3602801209954489.jpg', '2016-03-23 11:23:55', '2016-03-23 11:23:31', '1', '2', '0');
INSERT INTO `cms_place` VALUES ('27', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '19', '微软：不要在Win10中使用第三方“隐私保护”工具', '//www.publiccms.com/2015/08/06/19.html', '', '2016-03-23 11:27:26', '2016-03-23 11:27:06', '1', '0', '0');
INSERT INTO `cms_place` VALUES ('28', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '30', '女子吃了泡发2天的木耳 致多器官衰竭不治身亡', '//www.publiccms.com/2015/08-07/30.html', '', '2016-03-23 11:27:42', '2016-03-23 11:27:28', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('29', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '22', '江苏仪征新集一玩具厂起大火 火光冲天', '//www.publiccms.com/7/22.html', '', '2016-03-23 11:27:55', '2016-03-23 11:27:44', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('30', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '142', 'PublicCMS后台截图', '//www.publiccms.com/9/142.html', '', '2016-03-23 11:28:08', '2016-03-23 11:27:57', '1', '6', '0');
INSERT INTO `cms_place` VALUES ('31', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '18', 'PublicCMS进入测试阶段，即将正式发布', '//www.publiccms.com/9/18.html', '', '2016-03-23 11:28:21', '2016-03-23 11:28:14', '1', '7', '0');
INSERT INTO `cms_place` VALUES ('32', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '217', '酷冷至尊烈焰枪旗舰版机械键盘 有线104键游戏全背光 樱桃轴正品', 'http://s.click.taobao.com/t?e=m%3D2%26s%3Dk%2FRaMwaPpnYcQipKwQzePOeEDrYVVa64K7Vc7tFgwiFRAdhuF14FMV3pVOinSGgeRitN3%2FurF3zO1KWqeCJhFmPYiLpdxhFe%2B6GA20g%2FvatSQhIbbzwChQUDqeizZVd13GFiMU8U2DTHAGIcyZQCxSGFCzYOOqAQ&pvid=50_106.2.199.138_346_1458707425019', '', '2016-03-28 11:21:01', '2016-03-28 11:17:37', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('33', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, 'BD工具网', 'http://www.bdtool.net/', '', '2016-03-28 14:29:39', '2016-03-28 14:29:34', '1', '23', '0');
INSERT INTO `cms_place` VALUES ('34', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, '在线项目计划', 'http://www.oiplan.com/user/index.do', '', '2016-03-28 16:35:01', '2016-03-28 16:34:48', '1', '25', '0');
INSERT INTO `cms_place` VALUES ('35', '1', '/index.html/5cf1b463-8d14-4ba4-a904-890ec224dc99.html', '1', 'custom', null, '动态站点', '//cms.publiccms.com/', '', '2016-03-31 18:50:06', '2016-03-31 18:49:54', '1', '16', '1');
INSERT INTO `cms_place` VALUES ('36', '1', '/index.html/d1bef19f-ec32-4c3b-90f9-b25ca0fe19e3.html', '1', 'custom', null, '成品网站模板超市', 'http://www.edge-cloud.cn/wangzhanjianshe-30-1.html', '', '2016-04-13 16:05:14', '2016-04-13 16:04:55', '1', '2', '0');
INSERT INTO `cms_place` VALUES ('37', '2', '/index.html/3fa1a038-e006-459b-a94d-65ec19a6df16.html', '6', 'custom', NULL, '<i class=\"icon-phone\"></i>+ 400 888 9999', 'tel://400 888 9999', '', '2017-02-14 14:01:41', '2017-02-14 14:01:08', '1', '8', '0');
INSERT INTO `cms_place` VALUES ('38', '2', '/index.html/0a05d34a-d102-4604-9ba8-86c9536c4222.html', '6', 'custom', NULL, '一心为你', '#', 'upload/2017/02-25/08-39-510385-206164567.jpg', '2017-02-14 13:37:30', '2017-02-14 13:37:04', '1', '164', '0');
INSERT INTO `cms_place` VALUES ('39', '2', '/index.html/0a05d34a-d102-4604-9ba8-86c9536c4222.html', '6', 'custom', NULL, '一心服务', '#', 'upload/2017/02-25/08-39-2305771016946698.jpg', '2017-02-14 13:36:56', '2017-02-14 13:36:15', '1', '26', '0');
INSERT INTO `cms_place` VALUES ('40', '2', '/index.html/0a05d34a-d102-4604-9ba8-86c9536c4222.html', '6', 'custom', NULL, '一心追求', '#', 'upload/2017/02-25/08-40-040072-1644893878.jpg', '2017-02-14 13:33:52', '2017-02-14 13:32:58', '1', '218', '0');

-- ----------------------------
-- Records of cms_place_attribute
-- ----------------------------
INSERT INTO `cms_place_attribute` VALUES ('1', '{}');
INSERT INTO `cms_place_attribute` VALUES ('2', '{}');
INSERT INTO `cms_place_attribute` VALUES ('3', '{}');
INSERT INTO `cms_place_attribute` VALUES ('4', '{}');
INSERT INTO `cms_place_attribute` VALUES ('5', '{}');
INSERT INTO `cms_place_attribute` VALUES ('6', '{}');
INSERT INTO `cms_place_attribute` VALUES ('7', '{}');
INSERT INTO `cms_place_attribute` VALUES ('8', '{}');
INSERT INTO `cms_place_attribute` VALUES ('9', '{}');
INSERT INTO `cms_place_attribute` VALUES ('10', '{}');
INSERT INTO `cms_place_attribute` VALUES ('11', '{}');
INSERT INTO `cms_place_attribute` VALUES ('12', '{}');
INSERT INTO `cms_place_attribute` VALUES ('13', '{}');
INSERT INTO `cms_place_attribute` VALUES ('14', '{}');
INSERT INTO `cms_place_attribute` VALUES ('15', '{}');
INSERT INTO `cms_place_attribute` VALUES ('16', '{}');
INSERT INTO `cms_place_attribute` VALUES ('17', '{}');
INSERT INTO `cms_place_attribute` VALUES ('18', '{}');
INSERT INTO `cms_place_attribute` VALUES ('19', '{}');
INSERT INTO `cms_place_attribute` VALUES ('20', '{}');
INSERT INTO `cms_place_attribute` VALUES ('21', '{}');
INSERT INTO `cms_place_attribute` VALUES ('22', '{}');
INSERT INTO `cms_place_attribute` VALUES ('23', '{}');
INSERT INTO `cms_place_attribute` VALUES ('24', '{}');
INSERT INTO `cms_place_attribute` VALUES ('25', '{}');
INSERT INTO `cms_place_attribute` VALUES ('26', '{}');
INSERT INTO `cms_place_attribute` VALUES ('27', '{}');
INSERT INTO `cms_place_attribute` VALUES ('28', '{}');
INSERT INTO `cms_place_attribute` VALUES ('29', '{}');
INSERT INTO `cms_place_attribute` VALUES ('30', '{}');
INSERT INTO `cms_place_attribute` VALUES ('31', '{}');
INSERT INTO `cms_place_attribute` VALUES ('32', '{}');
INSERT INTO `cms_place_attribute` VALUES ('33', '{}');
INSERT INTO `cms_place_attribute` VALUES ('34', '{}');
INSERT INTO `cms_place_attribute` VALUES ('35', '{}');
INSERT INTO `cms_place_attribute` VALUES ('36', '{}');
INSERT INTO `cms_place_attribute` VALUES ('37', '{}');
INSERT INTO `cms_place_attribute` VALUES ('38', '{\"price\":\"$ Beyond Price\",\"description\":\"一心一意做事，一心一意为您和您的家庭，我们是一群向往清新空气、蔚蓝天空和阳光沙滩的人们，我们希望一路上也有您的参与和陪伴。\"}');
INSERT INTO `cms_place_attribute` VALUES ('39', '{\"price\":\"Beyond Price\",\"description\":\"专业的服务，专业的品质，从“咨询”到“看房”再到“购房”，一心将为您提供最精致最贴心的服务。我们的态度就是我们的产品。\"}');
INSERT INTO `cms_place_attribute` VALUES ('40', '{\"price\":\"Beyond Price\",\"description\":\"感谢同我们一样追求更美人生的你，不断提高、不断进步、不断创造将使你我变得更加与众不同。\"}');

-- ----------------------------
-- Records of cms_tag
-- ----------------------------

INSERT INTO `cms_tag` VALUES ('1', '1', 'PublicCMS', null, 0);
