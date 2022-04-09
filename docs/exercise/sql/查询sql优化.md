-- 优化前：
``` sql
select 各查询字段
from `表名`
where 过滤条件
limit 0,10;
```

-- 优化后：
``` sql
select 各查询字段
from `表名` as main_table
right join(
    select 主键字段
    from `表名`
    where 过滤条件
    limit 0,10;
) as temp_table 
on temp_table.主键字段=main_table.主键字段
```

在使用limit的语句中只返回主键结果，使用主键值直接获取其他需要的字段值，减少回表操作。