drop table if exists user;

create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    user_account  varchar(256)                       null comment '账号',
    user_password varchar(512)                       not null comment '密码',
    phone         varchar(128)                       null comment '电话',
    email         varchar(512)                       null comment '邮箱',
    user_role     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    nickname      varchar(256)                       null comment '用户昵称',
    avatar_url    varchar(1024)                      null comment '用户头像',
    gender        tinyint                            null comment '性别',
    game_id       varchar(128)                       null comment '游戏号（表名）',
    user_status   tinyint  default 0                 not null comment '状态 0 - 正常',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       tinyint  default 0                 not null comment '逻辑删除'
)
    comment '用户';