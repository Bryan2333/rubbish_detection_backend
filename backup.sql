CREATE DATABASE IF NOT EXISTS `rubbish_detection_system`;

USE `rubbish_detection_system`;

CREATE TABLE users
(
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    username             VARCHAR(50)  NOT NULL UNIQUE,             -- 用户名，登录时使用
    email                VARCHAR(100) NOT NULL UNIQUE,             -- 邮箱地址
    password             VARCHAR(255) NOT NULL,                    -- 密码应当以哈希形式存储
    age                  TINYINT UNSIGNED,                         -- 年龄
    gender               ENUM ('男', '女', '保密') DEFAULT '保密', -- 性别
    signature            VARCHAR(255),                             -- 个性签名（选填）
    avatar               VARCHAR(255),                             -- 头像图片路径（可以存放图片 URL 或本地路径）
    participation_count  INT                       DEFAULT 0,      -- 参与回收次数
    total_recycle_amount DECIMAL(10, 2)            DEFAULT 0.00,   -- 累计回收金额（单位：人民币）
    created_at           TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE feedback
(
    id          INT UNSIGNED NOT NULL AUTO_INCREMENT, -- 唯一自增主键
    name        VARCHAR(100) NOT NULL,                -- 用户姓名，建议长度 100 个字符
    email       VARCHAR(150) NOT NULL,                -- 用户邮箱，建议长度 150 个字符
    content     TEXT         NOT NULL,                -- 反馈内容，使用 TEXT 类型保存详细描述
    submit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 提交时间，默认保存当前时间
    PRIMARY KEY (id)
);

CREATE TABLE banner
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT,                            -- 唯一自增主键
    image_path VARCHAR(255) NOT NULL,                                           -- 图片 URL
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                             -- 创建时间，默认保存当前时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间，默认保存当前时间
    PRIMARY KEY (id)
);

CREATE TABLE recognition_collection
(
    id           INT UNSIGNED NOT NULL AUTO_INCREMENT,                             -- 唯一自增主键
    user_id      INT          NOT NULL,                                            -- 用户 ID
    image_path   VARCHAR(255) NOT NULL,                                            -- 图片 URL
    rubbish_type INT UNSIGNED NOT NULL,                                            -- 垃圾类型
    created_at   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,                             -- 创建时间，默认保存当前时间
    updated_at   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间，默认保存当前时间
    is_deleted   TINYINT(1) DEFAULT 0,                                             -- 是否已删除，默认为 0
    PRIMARY KEY (id)
);

CREATE TABLE addresses
(
    id          INT AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    phone_num   VARCHAR(20)  NOT NULL,
    province    VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,
    area        VARCHAR(255) NOT NULL,
    detail      VARCHAR(255) NOT NULL,
    pickup_time DATETIME     NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE wastes
(
    id          INT,
    type        VARCHAR(255)   NOT NULL,
    name        VARCHAR(255)   NOT NULL,
    weight      DECIMAL(10, 2) NOT NULL,
    unit        VARCHAR(50)    NOT NULL,
    description VARCHAR(255)   NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE waste_photos
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    waste_id   INT          NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (waste_id) REFERENCES wastes (id) ON DELETE CASCADE
);

CREATE TABLE orders
(
    id              INT PRIMARY KEY,
    user_id         INT            NOT NULL,
    address_id      INT            NOT NULL,
    waste_id        INT            NOT NULL,
    order_date      DATE           NOT NULL,
    order_status    INT            NOT NULL,
    estimated_price DECIMAL(10, 2) NOT NULL,
    actual_price    DECIMAL(10, 2) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (address_id) REFERENCES addresses (id) ON DELETE CASCADE,
    FOREIGN KEY (waste_id) REFERENCES wastes (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


-- 插入mock数据到 addresses 表
INSERT INTO addresses (name, phone_num, province, city, area, detail, pickup_time)
VALUES ('张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号', '2024-12-25 10:00:00'),
       ('李四', '13900139000', '上海市', '上海市', '浦东新区', '世纪大道100号', '2024-12-26 14:30:00');

-- 插入mock数据到 wastes 表
INSERT INTO wastes (id, type, name, weight, unit, description)
VALUES (101, '可回收物', '纸张', 5.00, '千克', '废旧报纸和杂志'),
       (201, '湿垃圾', '剩菜剩饭', 3.00, '千克', '家庭剩余食物');

-- 插入mock数据到 waste_photos 表
INSERT INTO waste_photos (waste_id, image_path)
VALUES (101, 'https://example.com/photos/paper1.jpg'),
       (101, 'https://example.com/photos/paper2.jpg'),
       (201, 'https://example.com/photos/food1.jpg'),
       (201, 'https://example.com/photos/food2.jpg');

-- 插入mock数据到 orders 表
INSERT INTO orders (orders.user_id, address_id, waste_id, order_date, order_status, estimated_price, actual_price)
VALUES (6, 1, 101, '2024-12-25', 1, 10.50, 0),
       (6, 2, 201, '2024-12-26', 3, 0.00, 0);


SELECT o.id                       AS id,
       o.user_id                  AS userId,
       o.order_date               AS orderDate,
       o.order_status             AS orderStatus,
       o.estimated_price          AS estimatedPrice,
       o.actual_price             AS actualPrice,
       o.created_at               AS createdAt,
       o.updated_at               AS updatedAt,
       a.id                       AS address_id,
       a.name                     AS address_name,
       a.phone_num                AS address_phoneNum,
       a.province                 AS address_province,
       a.city                     AS address_city,
       a.area                     AS address_area,
       a.detail                   AS address_detail,
       a.pickup_time              AS address_pickupTime,
       w.id                       AS waste_id,
       w.type                     AS waste_type,
       w.name                     AS waste_name,
       w.weight                   AS waste_weight,
       w.unit                     AS waste_unit,
       w.description              AS waste_description,
       (SELECT JSON_ARRAYAGG(image_path)
        FROM waste_photos wp
        WHERE wp.waste_id = w.id) AS photoUrl
FROM orders o
         JOIN addresses a ON o.address_id = a.id
         JOIN wastes w ON o.waste_id = w.id
WHERE o.user_id = 6
ORDER BY o.order_date DESC
LIMIT 2 OFFSET 0;

SELECT o.id              AS id,
       o.user_id         AS userId,
       o.order_date      AS orderDate,
       o.order_status    AS orderStatus,
       o.estimated_price AS estimatedPrice,
       o.actual_price    AS actualPrice,
       o.created_at      AS createdAt,
       o.updated_at      AS updatedAt,
       JSON_OBJECT(
               'id', a.id,
               'name', a.name,
               'phone_num', a.phone_num,
               'province', a.province,
               'city', a.city,
               'area', a.area,
               'detail', a.detail,
               'pickup_time', a.pickup_time
       )                 AS address,
       JSON_OBJECT(
               'id', w.id,
               'type', w.type,
               'name', w.name,
               'weight', w.weight,
               'unit', w.unit,
               'description', w.description,
               'photos', (SELECT JSON_ARRAYAGG(wp.image_path)
                          FROM waste_photos wp
                          WHERE wp.waste_id = w.id)
       )                 AS waste
FROM orders o
         JOIN
     addresses a ON o.address_id = a.id
         JOIN
     wastes w ON o.waste_id = w.id;

SELECT o.id              AS id,
       o.user_id         AS userId,
       o.order_date      AS orderDate,
       o.order_status    AS orderStatus,
       o.estimated_price AS estimatedPrice,
       o.actual_price    AS actualPrice,
       o.created_at      AS createdAt,
       o.updated_at      AS updatedAt,
       JSON_OBJECT(
               'id', a.id,
               'name', a.name,
               'phoneNum', a.phone_num,
               'province', a.province,
               'city', a.city,
               'area', a.area,
               'detail', a.detail,
               'pickupTime', a.pickup_time
       )                 AS address,
       JSON_OBJECT(
               'id', w.id,
               'type', w.type,
               'name', w.name,
               'weight', w.weight,
               'unit', w.unit,
               'description', w.description,
               'photos', (SELECT JSON_ARRAYAGG(wp.image_path)
                          FROM waste_photos wp
                          WHERE wp.waste_id = w.id)
       )                 AS waste
FROM orders o
         JOIN
     addresses a ON o.address_id = a.id
         JOIN
     wastes w ON o.waste_id = w.id
WHERE o.user_id = 6
  AND o.order_date > DATE_SUB(CURDATE(), INTERVAL 7 DAY)
  AND o.order_date <= CURDATE()
ORDER BY o.order_date DESC
