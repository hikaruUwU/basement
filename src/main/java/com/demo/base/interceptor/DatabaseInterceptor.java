package com.demo.base.interceptor;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.table.TableInfoFactory;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DatabaseInterceptor extends CommonsDialectImpl implements MyBatisFlexCustomizer {
    private static final String SEPARATOR = "@Separator@";
    private static final Map<String, Function<QueryWrapper, QueryWrapper>> SINGLE_TABLE_STRATEGIES = Map.ofEntries(
    //        Map.entry(TableOf(User.class), queryWrapper -> queryWrapper.eq(User::getUuid, "specified"))
    );
    private static final Map<String, Function<QueryWrapper, QueryWrapper>> JOIN_TABLE_STRATEGIES = Map.ofEntries(
    //        Map.entry(TableOf(User.class, User.class), queryWrapper -> queryWrapper.eq(User::getUuid, "specified"))
    );

    @Override
    public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
        List<QueryTable> queryTables = CPI.getQueryTables(queryWrapper);

        if (queryTables != null && !queryTables.isEmpty()) {
            if (queryTables.size() == 1) {
                applyStrategy(queryTables.getFirst().getName(), SINGLE_TABLE_STRATEGIES, queryWrapper);
            } else {
                String joinKey = queryTables.stream().map(QueryTable::getName).sorted().collect(Collectors.joining(SEPARATOR));
                applyStrategy(joinKey, JOIN_TABLE_STRATEGIES, queryWrapper);
            }
        }

        super.prepareAuth(queryWrapper, operateType);
    }

    private void applyStrategy(String key, Map<String, Function<QueryWrapper, QueryWrapper>> strategies, QueryWrapper qw) {
        Function<QueryWrapper, QueryWrapper> strategy = strategies.get(key);
        if (strategy != null) {
            strategy.apply(qw);
        }
    }

    private static String TableOf(Class<?> entity) {
        return TableInfoFactory.ofEntityClass(entity).getTableName();
    }

    private static String TableOf(Class<?>... entities) {
        return Arrays.stream(entities).map(e -> TableInfoFactory.ofEntityClass(e).getTableName()).sorted().collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        if (!SINGLE_TABLE_STRATEGIES.isEmpty() || !JOIN_TABLE_STRATEGIES.isEmpty()) {
            DialectFactory.registerDialect(DbType.MYSQL, this);
        }
    }
}