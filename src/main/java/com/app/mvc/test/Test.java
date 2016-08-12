package com.app.mvc.test;import com.google.common.base.Joiner;import com.google.common.collect.Lists;import lombok.extern.slf4j.Slf4j;import java.io.BufferedReader;import java.io.File;import java.io.FileReader;import java.io.FileWriter;import java.net.URL;import java.util.List;/** * Created by jimin on 16/7/9. */@Slf4jpublic class Test {    public static void main(String[] args) throws Exception {        URL user1 = Test.class.getClassLoader().getResource("user1.txt");        List<Integer> user1IdList = readFile(user1);        FileWriter fileWriter=new FileWriter("result.sql");        int limit = 1000;        int total = (user1IdList.size() + limit - 1) / limit; // 按照limit要生成的条数        for(int i = 0; i < total; i++) {            // 本次开始节点            int from = i * limit;            // 本次结束节点            int to = from + limit;            if(to >= user1IdList.size()) { // 防止最后一次越界                to = user1IdList.size();            }            // 本地处理的数据            List<Integer> list = user1IdList.subList(from, to);            String userIdStr = Joiner.on(",").join(list);            log.info("delete from stock_passport.users where user_id in (" + userIdStr +") and user_id not in (select UserID from s_common_us.users);");            fileWriter.write("delete from stock_passport.user_idcard where user_id in (" + userIdStr +") and user_id not in (select UserID from s_common_us.users);\n");        }        fileWriter.flush();        fileWriter.close();        log.info("total:{}, size:{}", total, user1IdList.size());    }    public static List<Integer> readFile(URL url) throws Exception {        BufferedReader br = null;        List<Integer> list = Lists.newArrayList();        try {            File file = new File(url.toURI());            br = new BufferedReader(new FileReader(file));            String line = null;            while ((line = br.readLine()) != null) {                list.add(Integer.valueOf(line));            }        } catch (Exception e) {            throw e;        }        return list;    }}