drop table if exists room;

create table room
(
    id            varchar(256)                       not null
        primary key,
    owner         varchar(256)                       not null comment '房主',
    partner       varchar(256)                       null comment '参与者',
    room_status   tinyint  default 0                 not null comment '游戏房间状态（0-未启用，1-等待玩家加入，2-游戏进行中，3-游戏已结束）',
    first_player  varchar(256)                       null comment '先手方',
    numbers       varchar(1024)                      null comment '游戏数字（JSON {序号: 数据}）',
    winner        tinyint  default 0                 null comment '获胜方（0-胜负未分，1-房主获胜，2-参与者获胜）',
    round         int                                null comment '回合数',
    owner_skill   varchar(1024)                      null comment '房主技能列表（JSON {技能: 数量}）',
    partner_skill varchar(1024)                      null comment '参与者技能（JSON {技能: 数量}）',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       tinyint  default 0                 not null comment '逻辑删除'
)
    comment '游戏房间表';