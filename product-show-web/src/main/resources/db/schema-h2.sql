CREATE DATABASE product_show_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use product_show_db;
drop table if exists dictionary;
create table dictionary
(
    id             varchar(32) not null comment '主键',
    created_by     varchar(32) comment '创建人',
    created_time   datetime    not null comment '创建时间',
    updated_by     varchar(32) comment '更新人',
    updated_time   datetime    not null comment '更新时间',
    dic_type       varchar(32) not null comment '类型',
    dic_parent_key varchar(32) not null comment '父级节点key',
    dic_key        varchar(32) not null comment 'key',
    dic_value      varchar(64) not null comment 'value',
    primary key (id)
) comment = '字典';

# 文件信息
drop table if exists file_info;
create table file_info
(
    id           varchar(32) not null comment '主键',
    created_by   varchar(32) comment '创建人',
    created_time datetime    not null comment '创建时间',
    updated_by   varchar(32) comment '更新人',
    updated_time datetime    not null comment '更新时间',
    file_md5     varchar(64) not null comment 'md5',
    real_path    text comment '文件真实路径',
    product_id   varchar(32) comment '关联产品信息',
    order_num    int default 0 comment '顺序',
    file_size    long comment '文件大小',
    before_name  varchar(128) comment '原始文件名',
    file_type    varchar(32) comment '文件类型',
    primary key (id)
) comment = '文件信息';

# 产品信息
drop table if exists product_show_db.product_info;
create table product_show_db.product_info
(
    id                     varchar(32)  not null comment '主键'
        primary key,
    created_by             varchar(32)  null comment '创建人',
    created_time           datetime     not null comment '创建时间',
    updated_by             varchar(32)  null comment '更新人',
    updated_time           datetime     not null comment '更新时间',
    product_name           varchar(128) null comment '产品名称',
    product_business       varchar(128) null comment '所属行业',
    product_area           varchar(128) null comment '所属区域',
    product_linkman_tel    varchar(32)  null comment '产品联系人电话',
    product_linkman        varchar(32)  null comment '产品联系人',
    product_introduction   text         null comment '产品简介',
    product_light_spot     text         null comment '产品亮点',
    like_total             integer      not null default 0 comment '点赞',
    watch_total            integer      not null default 0 comment '观看',
    product_home_image_md5 varchar(32)  null comment '产品首页展示图MD5'
)
    comment '产品信息';

drop table if exists product_file_unique;
create table product_file_unique
(
    id         varchar(32)  not null comment '主键'
        primary key,
    product_id varchar(32)  not null comment '产品id',
    file_md5   varchar(32)  not null comment '文件id',
    file_order integer      not null default 0 comment '文件顺序',
    detail     varchar(255) null comment '描述',
    file_type  varchar(32)  not null comment '文件类型',
    constraint product_file_unique_pk
        unique (product_id, file_md5)
);

drop table if exists t_user;
create table t_user
(
    id           varchar(32) not null comment '主键'
        primary key,
    created_by   varchar(32) comment '创建人',
    created_time datetime    not null comment '创建时间',
    updated_by   varchar(32) comment '更新人',
    updated_time datetime    not null comment '更新时间',
    user_name    varchar(32) comment '用户名',
    password     varchar(64) comment '密码',
    phone_number varchar(64) comment '电话号码'
) comment '用户信息';
INSERT INTO product_show_db.t_user (id, created_by, created_time, updated_by, updated_time, user_name, password,
                                    phone_number)
VALUES ('574614967464cdeasjwf', null, '2021-05-24 13:47:40', null, '2021-05-24 13:47:49', 'admin', '123456', null);

drop table if exists t_comment;
create table t_comment
(
    id           varchar(32) not null comment '主键'
        primary key,
    created_by   varchar(32) comment '创建人',
    created_time datetime    not null comment '创建时间',
    updated_by   varchar(32) comment '更新人',
    updated_time datetime    not null comment '更新时间',
    user_name    varchar(32) comment '用户名',
    comment      text comment '评论',
    ip           varchar(32) comment 'ip',
    product_id   varchar(32) not null comment '产品id'
) comment '评论信息';

drop table if exists user_like;
create table `user_like`
(
    id                varchar(32) not null comment '主键'
        primary key,
    liked_target_type varchar(32) not null comment '被点赞的类型',
    liked_target_id   varchar(32) not null comment '被点赞的id',
    liked_user_id     varchar(32) not null comment '点赞的用户id',
    status            tinyint(1)           default '1' comment '点赞状态，0取消，1点赞',
    created_time      timestamp   not null default current_timestamp comment '创建时间',
    updated_time      timestamp   not null default current_timestamp on update current_timestamp comment '更新时间',
    INDEX index_liked_target_type (liked_target_type),
    INDEX index_liked_target_id (liked_target_id)
) comment '用户点赞表';

# 更新20210601
# 增加字典联合主键
alter table dictionary
    add constraint uk_01
        unique (dic_parent_key, dic_key, dic_type);

# 更新20210602
alter table product_info
    add version int default 0 not null comment '版本';

# 20210604 增加字段
alter table product_info
    add platform_and_module varchar(128) null comment '项目相关平台及组件';

# 20210604 改变字段长度
alter table dictionary
    modify dic_parent_key varchar(128) not null comment '父级节点key';
alter table dictionary
    modify dic_key varchar(128) not null comment 'key';

# 20210608 增加文件信息表字段
alter table t_comment
    add c_parent_id varchar(32) not null default '0' comment '评论父级id';
alter table file_info
    add new_name varchar(128) null comment '文件新名称';
alter table file_info
    add file_suffix varchar(32) null comment '文件后缀';

# 20210609 删除无用的字段
alter table file_info
    drop column product_id;
alter table file_info
    drop column order_num;
