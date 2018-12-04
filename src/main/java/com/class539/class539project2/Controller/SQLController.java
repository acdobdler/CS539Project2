package com.class539.class539project2.Controller;

import com.class539.class539project2.Service.BigQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/db")
public class SQLController {

    @Autowired
    @Qualifier("msSqlConnector")
    private JdbcTemplate msSqlConnector;

    @Autowired
    @Qualifier("bigQueryService")
    private BigQueryService bigQueryService;

    @RequestMapping("/query")
    public List<Map<String, Object>> query(String sql, int flag) throws Exception {
        if(flag == 0) {
            return bigQuery(sql);
        } else {
            return queryMSSQL(sql);
        }
    }

    @RequestMapping("/update")
    public List<Map<String, Object>> update(String sql, int flag) throws Exception {
        if(flag == 0) {
            return bigQuery(sql);
        } else {
            return updateMSSQL(sql);
        }
    }

    private List<Map<String, Object>> bigQuery(String sql) throws Exception {
//        String[] sqls = sql.toLowerCase().split("from");
//        String newSQL = sqls[0] + "from cs539_instacart." + sqls[1].trim();
        System.out.println(sql);
        return bigQueryService.querySQL(sql);
    }

    private List<Map<String, Object>> queryMSSQL(String sql) {
        System.out.println(sql);
        return msSqlConnector.queryForList(sql);
    }

    private List<Map<String, Object>> updateMSSQL(String sql) {
        System.out.println(sql);
        int res = msSqlConnector.update(sql);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("res", res);
        list.add(map);
        return list;
    }
}