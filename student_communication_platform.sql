/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : student_communication_platform

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 18/12/2024 22:48:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子内容',
  `category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类（如寻物启事、技能咨询、资源共享等）',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `reply_count` int NULL DEFAULT 0 COMMENT '回复数',
  `is_hot` tinyint(1) NULL DEFAULT 0 COMMENT '是否热门',
  `is_reported` tinyint(1) NULL DEFAULT 0 COMMENT '是否被举报',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, 'Lost and Found: Missing Student ID', 'I lost my student ID in the library yesterday. The card number is 123456. Please contact me if you find it. Thank you!', 'Lost and Found', '/api/attachment/FILE/40bc6663b0ca06afdd5ecb587818ee53.webp', 5, 2, 0, 0, '2024-12-18 15:43:17', 'user1');
INSERT INTO `post` VALUES (2, 'Skill Advice: How to Learn Programming', 'I want to learn programming but don\'t know where to start. Any advice from experienced programmers?', 'Skill Advice', '/api/attachment/FILE/2af39c026bd822254db997378ae42ea7.jpg', 10, 3, 1, 0, '2024-12-18 15:43:17', 'user2');
INSERT INTO `post` VALUES (3, 'Resource Sharing: Free E-books', 'Sharing some free e-books on programming and design. Feel free to download them.', 'Resource Sharing', '/api/attachment/FILE/2c6ecbf62d28620096c9e99a33fe43e4.webp', 8, 1, 0, 0, '2024-12-18 15:43:17', 'user3');
INSERT INTO `post` VALUES (4, 'Lost and Found: Missing Keys', 'I lost a set of keys on the playground. It has a small bear keychain. Please contact me if you find it.', 'Lost and Found', '/api/attachment/FILE/5124691f32d08dcef7ed484e41153d87.webp', 3, 1, 0, 0, '2024-12-18 15:43:17', 'user4');
INSERT INTO `post` VALUES (5, 'Skill Advice: How to Improve English Speaking', 'I want to improve my English speaking skills. Any good methods to recommend?', 'Skill Advice', '/api/attachment/FILE/7b8ccf93af317e85a127fababe6b14a5.jpg', 7, 2, 0, 0, '2024-12-18 15:43:17', 'user5');
INSERT INTO `post` VALUES (6, 'Resource Sharing: Free Design Assets', 'Sharing some free design assets, including icons and background images.', 'Resource Sharing', '/api/attachment/FILE/d6082de1f2a9533a7dd681828c8f4dcb.webp', 6, 1, 0, 0, '2024-12-18 15:43:17', 'user6');
INSERT INTO `post` VALUES (7, 'Lost and Found: Missing Phone', 'I lost my phone in the cafeteria. It\'s an iPhone 12. Please contact me if you find it.', 'Lost and Found', '/api/attachment/FILE/0ac3b5cd5479397d5cf5c3a7235f6f43.webp', 4, 1, 0, 0, '2024-12-18 15:43:17', 'user7');
INSERT INTO `post` VALUES (8, 'Skill Advice: How to Learn Photography', 'I want to learn photography. Any good tutorials to recommend?', 'Skill Advice', '/api/attachment/FILE/88d9760ad39a42c375edf4d5a60386b5.webp', 9, 2, 1, 0, '2024-12-18 15:43:17', 'user8');
INSERT INTO `post` VALUES (9, 'Resource Sharing: Free Programming Courses', 'Sharing some free programming courses suitable for beginners.', 'Resource Sharing', '/api/attachment/FILE/bef0c34ada1df1d9744fe1296e81afae.webp', 6, 2, 0, 0, '2024-12-18 15:43:17', 'user9');
INSERT INTO `post` VALUES (10, 'Lost and Found: Missing Earbuds', 'I lost my AirPods Pro in the classroom. Please contact me if you find them.', 'Lost and Found', '/api/attachment/FILE/89c5b25567f5a40f2b541a9336bbe8b5.webp', 2, 1, 1, 0, '2024-12-18 15:43:17', 'user2');
INSERT INTO `post` VALUES (22, 'Looking for a Study Partner', 'I\'m looking for a study partner to learn English together and keep each other motivated.', 'Study Group', '/api/attachment/FILE/462fd39985c5507078376e879dcd48bc.webp', 8, 3, 0, 0, '2024-12-18 15:43:17', 'user2');
INSERT INTO `post` VALUES (23, 'Campus Job Fair Information', 'There will be a large job fair next Wednesday. Don\'t forget to prepare your resumes in advance.', 'Campus Events', '/api/attachment/FILE/73059f573a8480abc765da58a8011771.webp', 20, 7, 1, 0, '2024-12-18 15:43:17', 'user3');
INSERT INTO `post` VALUES (24, 'Dorm Network Issues', 'The dorm network has been frequently disconnecting recently. Has anyone else experienced the same issue?', 'Life Help', '/api/attachment/FILE/45fbaf1b9de7f155d749e10a71133028.webp', 5, 2, 0, 0, '2024-12-18 15:43:17', 'user4');
INSERT INTO `post` VALUES (25, 'Recommended Note-Taking App', 'Notion is really useful. I highly recommend it to everyone.', 'Resource Sharing', '/api/attachment/FILE/4b066ac1fe552cbd942d95d9fd9f543a.jpg', 15, 4, 0, 0, '2024-12-18 15:43:17', 'user5');
INSERT INTO `post` VALUES (26, 'Campus Food Recommendation', 'I recommend a really delicious snack shop near the school.', 'Life Sharing', '/api/attachment/FILE/5ad5b574c878758c3a4f8a4f39eb192e.webp', 10, 3, 0, 0, '2024-12-18 15:43:17', 'user6');
INSERT INTO `post` VALUES (27, 'Campus Cycling Event', 'We\'re organizing a campus cycling event this weekend. Interested students can sign up.', 'Campus Events', '/api/attachment/FILE/0f97b75804e8ffa62466f4765acaa2d2.webp', 18, 7, 0, 0, '2024-12-18 15:43:17', 'user8');
INSERT INTO `post` VALUES (28, 'Resource Sharing: Free Programming Courses', 'Sharing some free programming courses suitable for beginners.', 'Resource Sharing', '/api/attachment/FILE/5f43e9b807c022ce13a5b0ee6507b164.webp', 6, 2, 0, 0, '2024-12-18 15:43:17', 'user9');
INSERT INTO `post` VALUES (29, 'Dorm Decoration Tips', 'Sharing some tips for decorating your dorm to make it cozier.', 'Life Sharing', '/api/attachment/FILE/976186a16da7ebe9f2b40fc5079ec144.webp', 9, 3, 0, 0, '2024-12-18 15:43:17', 'user5');
INSERT INTO `post` VALUES (30, 'Campus Volunteer Recruitment', 'We\'re recruiting campus volunteers to participate in community service activities.', 'Campus Events', '/api/attachment/FILE/d11e08febbd1a9d8758227b36ce4b7e2.webp', 15, 5, 0, 0, '2024-12-18 15:43:17', 'user6');
INSERT INTO `post` VALUES (31, 'Resource Sharing: Free Study Materials', 'Sharing some free review materials for advanced mathematics and linear algebra.', 'Resource Sharing', '/api/attachment/FILE/894d13db085f17e95311d71db9201e71.webp', 11, 4, 0, 0, '2024-12-18 15:43:17', 'user8');
INSERT INTO `post` VALUES (32, 'Campus Photography Contest', 'The school will host a photography contest. Everyone is welcome to submit their work.', 'Campus Events', '/api/attachment/FILE/d1428398919e985781b1f32ef35e418d.webp', 22, 7, 1, 0, '2024-12-18 15:43:17', 'user9');
INSERT INTO `post` VALUES (33, 'Dorm Electrical Safety Tips', 'Sharing some safety tips for using electrical appliances in the dorm.', 'Life Sharing', '/api/attachment/FILE/6858d48213f0af4db7ab7610d105ef76.webp', 10, 2, 0, 0, '2024-12-18 15:43:17', 'user5');
INSERT INTO `post` VALUES (34, 'Looking for Internship Opportunities', 'I\'m looking for internship opportunities. Please leave a comment if you have any recommendations.', 'Job Hunting', '/api/attachment/FILE/729457f0c91c96d9ba198facfbc0a80a.webp', 13, 6, 0, 0, '2024-12-18 15:43:17', 'user6');
INSERT INTO `post` VALUES (35, 'Campus Basketball Game Sign-Up', 'The school basketball game is open for registration. Everyone is welcome to form a team and participate.', 'Campus Events', '/api/attachment/FILE/0c6d732a3fc9a235686b0af36e99f945.webp', 16, 5, 0, 0, '2024-12-18 15:43:17', 'user8');
INSERT INTO `post` VALUES (36, 'Study Methods Sharing', 'Sharing some effective study methods to help everyone improve efficiency.', 'Study Group', '/api/attachment/FILE/b8a5eb3417d8de5f69ca012bbe4ebe4e.webp', 19, 7, 0, 0, '2024-12-18 15:43:17', 'user9');
INSERT INTO `post` VALUES (37, 'Campus Housing Information', 'Sharing some housing information near the campus, suitable for new students.', 'Life Sharing', '/api/attachment/FILE/63e258c8e209eeb87cb55a148dfa50c3.webp', 17, 6, 0, 0, '2024-12-18 15:43:17', 'user8');
INSERT INTO `post` VALUES (38, 'Campus Library Hours Update', 'The library\'s opening hours have been adjusted. Please check the new schedule.', 'Campus Announcements', '/api/attachment/FILE/b4a95a2f1b7b3d98640f764e9201d928.webp', 23, 8, 1, 0, '2024-12-18 15:43:17', 'user9');
INSERT INTO `post` VALUES (39, 'Campus Environmental Protection Event', 'An environmental protection event will be held this Saturday. Everyone is welcome to participate.', 'Campus Events', '/api/attachment/FILE/cce6ab2d54bcaa41dee2cc261f444950.webp', 35, 10, 0, 0, '2024-12-18 15:43:17', 'user9');
INSERT INTO `post` VALUES (52, 'Life in Idlib hints at what Syria can expect from rebel rule', '<p><br></p><h1>Life in Idlib hints at what Syria can expect from rebel rule</h1><p>2 hours ago</p><p>Share</p><p>Save</p><p><span style=\"font-size: 16px;\">Hugo Bachega</span></p><p>Middle East correspondent</p><p><span style=\"font-size: 12px;\">Reporting fromIdlib, Syria</span></p><p><img src=\"https://ichef.bbci.co.uk/news/480/cpsprodpb/cc0e/live/74fbf5f0-bca5-11ef-b63a-f359206dd36b.jpg.webp\" alt=\"Reuters People walk along a busy street at a refugee camp in Idlib with several people buying vegetables from the back of a truck on 17 December\" data-href=\"\" style=\"width: 932px;\"/><span style=\"color: rgb(230, 232, 234); background-color: rgb(32, 34, 36); font-size: 12px;\">Reuters</span></p><p>Hayat Tahrir al-Sham brought some semblance of stability to Idlib in 2017 after years of civil war</p><p><br></p><p>The road to Idlib, a remote corner in north-west Syria, still has the signs of the old front lines: trenches, abandoned military positions, rocket shells and ammunition.</p><p>Until a little more than a week ago, this was the only area in the country controlled by the opposition.</p><p>From Idlib, rebels led by the Islamist group Hayat Tahrir al-Sham, or HTS, launched an astonishing offensive that toppled Bashar al-Assad and ended his family\'s five-decade dictatorship in Syria.</p><p>As a result, they have become the country\'s de facto authorities and appear to be trying to bring their way of governing to the rest of Syria.</p><p>In Idlib\'s city centre, opposition flags, with a green stripe and three red stars, were flying high in public squares and being waved by men and women, old and young, in the wake of Assad\'s removal. Graffiti on walls celebrated the resistance against the regime.</p><p>While destroyed buildings and piles of rubble were a reminder of the not-so-distant war, renovated houses, recently opened shops and well-maintained roads were testament that some things had, indeed, improved. But there were complaints of what was seen as heavy-handed rule by the authorities.</p><p>When we visited earlier this week, streets were relatively clean, traffic lights and lamp-posts worked, and officers were present in the busiest areas. Simple things absent in other parts of Syria, and a source of pride here.</p>', 'news', '/api/attachment/FILE/c8976ef8415427f929c8cf0295c8fddb.webp', 0, 0, 0, 0, '2024-12-18 16:07:20', 'admin');

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` int NOT NULL COMMENT '帖子ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复内容',
  `parent_id` int NULL DEFAULT NULL COMMENT '父回复ID（支持嵌套回复）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '回复人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply` VALUES (1, 1, 'I also saw a student ID in the library yesterday. It might be yours.', NULL, '2023-10-01 11:00:00', 'user2');
INSERT INTO `reply` VALUES (2, 1, 'Where exactly was it? I can help you look for it.', 1, '2023-10-01 11:10:00', 'user3');
INSERT INTO `reply` VALUES (3, 2, 'I suggest starting with Python. Its syntax is simple and great for beginners.', NULL, '2023-10-02 12:00:00', 'user4');
INSERT INTO `reply` VALUES (4, 2, 'I also think Python is a good choice. I recommend the book \"Python Crash Course\".', 3, '2023-10-02 12:10:00', 'user5');
INSERT INTO `reply` VALUES (5, 3, 'Thanks for sharing! I just needed these resources.', NULL, '2023-10-03 13:00:00', 'user6');
INSERT INTO `reply` VALUES (6, 4, 'I also saw a set of keys on the playground yesterday. It might be yours.', NULL, '2023-10-04 14:00:00', 'user7');
INSERT INTO `reply` VALUES (7, 5, 'You can try watching American TV shows. It helps with learning while watching.', NULL, '2023-10-05 15:00:00', 'user8');
INSERT INTO `reply` VALUES (8, 6, 'These assets are amazing! Thanks for sharing!', NULL, '2023-10-06 16:00:00', 'user9');
INSERT INTO `reply` VALUES (9, 7, 'I also saw a phone in the cafeteria yesterday. It might be yours.', NULL, '2023-10-07 17:00:00', 'user10');
INSERT INTO `reply` VALUES (10, 8, 'I recommend a website: Photographer\'s Site. It has many tutorials.', NULL, '2023-10-08 18:00:00', 'user1');
INSERT INTO `reply` VALUES (37, 27, 'how are you', NULL, '2024-12-18 16:06:13', 'admin');

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` int NOT NULL COMMENT '被举报帖子ID',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报原因',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '举报人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '举报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (1, 1, 'The post content is inaccurate and suspected to be false information.', '2023-10-01 11:30:00', 'user2');
INSERT INTO `report` VALUES (2, 2, 'The post content involves advertising and needs to be reviewed.', '2023-10-02 12:30:00', 'user3');
INSERT INTO `report` VALUES (3, 3, 'The post content contains inappropriate information and needs to be addressed.', '2023-10-03 13:30:00', 'user4');
INSERT INTO `report` VALUES (4, 4, 'The post content is inaccurate and suspected to be false information.', '2023-10-04 14:30:00', 'user5');
INSERT INTO `report` VALUES (5, 5, 'The post content involves advertising and needs to be reviewed.', '2023-10-05 15:30:00', 'user6');
INSERT INTO `report` VALUES (6, 6, 'The post content contains inappropriate information and needs to be addressed.', '2023-10-06 16:30:00', 'user7');
INSERT INTO `report` VALUES (7, 7, 'The post content is inaccurate and suspected to be false information.', '2023-10-07 17:30:00', 'user8');
INSERT INTO `report` VALUES (8, 8, 'The post content involves advertising and needs to be reviewed.', '2023-10-08 18:30:00', 'user9');
INSERT INTO `report` VALUES (9, 9, 'The post content contains inappropriate information and needs to be addressed.', '2023-10-09 19:30:00', 'user10');
INSERT INTO `report` VALUES (10, 10, 'The post content is inaccurate and suspected to be false information.', '2023-10-10 20:30:00', 'user1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简介',
  `role` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色',
  `post_count` int NULL DEFAULT 0 COMMENT '发帖数',
  `reply_count` int NULL DEFAULT 0 COMMENT '回复数',
  `follower_count` int NULL DEFAULT 0 COMMENT '关注者数',
  `following_count` int NULL DEFAULT 0 COMMENT '关注数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'user1', '123456', 'user1@example.com', 'Qu', '/api/attachment/FILE/689959f61a7277e6cfa26c5efe0bdf4b.webp', 'song junp baskboll', 'USER', 1, 1, 2, 2, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (2, 'user2', '123456', 'user2@example.com', '小红', '/api/attachment/FILE/c9ce5cf9d03208fb899cb1e4643c17b5.webp', '热爱摄影和旅行', 'USER', 3, 1, 0, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (3, 'user3', '123456', 'user3@example.com', '小李', '/api/attachment/FILE/bc7b89a93a7fc1eefd75efcca4c8553c.webp', '喜欢阅读和写作', 'USER', 2, 1, 0, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (4, 'user4', '123456', 'user4@example.com', '小张', '/api/attachment/FILE/4c4d47aa3d3944aeaa21c4ba0cd292ab.webp', '擅长数学和物理', 'USER', 2, 1, 0, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (5, 'user5', '123456', 'user5@example.com', '小王', '/api/attachment/FILE/9a82e35d41dbed2d890705977bc96a86.webp', '喜欢音乐和绘画', 'USER', 4, 1, 0, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (6, 'user6', '123456', 'user6@example.com', '小赵', '/api/attachment/FILE/d8d8043d887983918ad08c35a851d402.webp', '热爱运动和健身', 'USER', 4, 1, 0, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (7, 'user7', '123456', 'user7@example.com', '小刘', '/api/attachment/FILE/8bb7cda64dd127c552f6851b5b7f830e.webp', '喜欢电影和游戏', 'USER', 1, 1, 0, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (8, 'user8', '123456', 'user8@example.com', '小陈', '/api/attachment/FILE/28c94570aadd4cebd30a172a805d0781.webp', '擅长设计和编程', 'USER', 5, 1, 0, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (9, 'user9', '123456', 'user9@example.com', '小周', '/api/attachment/FILE/7be1b062797b25a0cd361fe2255900b5.webp', '热爱学习和分享', 'USER', 6, 1, 1, 1, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (10, 'user10', '123456', 'user10@example.com', '小吴', '/api/attachment/FILE/c629a8851cca4f4ca6789334d2b45654.webp', '喜欢摄影和美食', 'USER', 1, 1, 1, 0, '2024-12-13 14:37:50', 'admin');
INSERT INTO `sys_user` VALUES (11, 'admin', '123456', 'admin@qq.com', '管理员', '/api/attachment/FILE/72253c3905bce24402ecdc84663e2d76.jpg', '管理员你们注意点哈哈哈', 'ADMIN', 1, 0, 0, 1, '2024-12-13 14:37:50', NULL);

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `follower_id` int NOT NULL COMMENT '关注者ID',
  `followee_id` int NOT NULL COMMENT '被关注者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follow
-- ----------------------------
INSERT INTO `user_follow` VALUES (25, 1, 10, '2024-12-16 16:30:50');
INSERT INTO `user_follow` VALUES (27, 1, 9, '2024-12-16 18:25:34');
INSERT INTO `user_follow` VALUES (30, 11, 1, '2024-12-17 22:37:26');
INSERT INTO `user_follow` VALUES (31, 9, 1, '2024-12-18 16:07:31');

SET FOREIGN_KEY_CHECKS = 1;
