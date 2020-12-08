package com.example.demo.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 排名辅助算法类
 * @author Administrator
 */
public class SortRankUtil {

    /**
     * 根据 sortField 进行排序 11145668
     * @param listMap
     * @param sortType
     * @param sortField
     * @return
     */
    public static List<Map<String, Object>> listSortUtil(List<Map<String, Object>> listMap, int sortType, int sortField) {

        int index = 0;
        int no = 0;
        double lastScore = -1;
        String rankKey = (sortType == 1) ? "allRank" : "rank";
        String sortTypeStr = (sortField == 1) ? "sumScore" : "singleScore";
        try {
            if (listMap != null && listMap.size() > 1) {
                Collections.sort(listMap, (o1, o2) -> {
                    Double o1Value = Double.valueOf(o1.get(sortTypeStr).toString());
                    Double o2Value = Double.valueOf(o2.get(sortTypeStr).toString());
                    return o2Value.compareTo(o1Value);
                });
            }
            for (int i = 0; i < listMap.size(); i++) {
                Map<String, Object> s = listMap.get(i);
                if (Double.compare(lastScore, Double.valueOf(s.get(sortTypeStr).toString())) != 0) {
                    lastScore = Double.valueOf(s.get(sortTypeStr).toString());
                    index = index + 1 + no;
                    no = 0;
                } else {
                    no++;
                }
                listMap.get(i).put(rankKey, index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }

    /**
     * 对 List<Map<String, Object>> 集合中 根据某个 key 进行分组 并排序
     * @param list
     * @param key
     * @return
     */
    public static Map<String, List<Map<String, Object>>> groupingByKey(List<Map<String, Object>> list, String key) {
        Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();
        if (list.size() > 0) {
            Map<String, List<Map<String, Object>>> groupDate = list.stream().collect(Collectors.groupingBy(s -> String.valueOf(s.get(key))));
            groupDate.entrySet().stream().sorted(Map.Entry.<String, List<Map<String, Object>>>comparingByKey().reversed()).forEachOrdered(x -> {
                result.put(String.valueOf(x.getKey()), x.getValue());
            });
        }
        return result;
    }


    public static void main(String[] args) {
        testCode();

    }

    public static void testCode(){
        //构造数据
        List<Map<String, Object>> resultListMapResult = new ArrayList<>();
        List<Map<String,Object>> sortListMap= new ArrayList<>();
        for(int i=1;i<=10;i++){
            int x=10;
            int y=100;
            Random random=new Random();
            // 返回x to y之间的一个随机数，不仅可以取到下界x还可以取到上界y，即返回[x,y]的闭区间。
            int rd=random.nextInt(y-x+1)+x;
            Map<String,Object> map=new HashMap();
            String name="name"+i;
            map.put("name",name);
            map.put("age",i);
            map.put("sumScore",rd);
            map.put("singleScore",rd+i);
            if(i%2==0){
                map.put("dept",0);
            }else{
                map.put("dept",1);
            }
            sortListMap.add(map);
        }
        //调用方法
        Map<String, List<Map<String, Object>>> deptMap = groupingByKey(sortListMap, "dept");
        System.out.println("分组后的数据。。。。。");

        Set<String> strings = deptMap.keySet();
        for(String str : strings){
            List<Map<String, Object>> maps = deptMap.get(str);
            //排序
            List<Map<String, Object>> maps1 = listSortUtil(maps, 1, 1);
            resultListMapResult.addAll(maps1);
        }

        for(int i=0;i<resultListMapResult.size();i++){
            Map<String, Object> stringObjectMap = resultListMapResult.get(i);
            System.out.print(stringObjectMap.get("name")+" ");
            System.out.print(stringObjectMap.get("sumScore")+" ");
            System.out.print(stringObjectMap.get("allRank")+" ");
            System.out.print(stringObjectMap.get("dept")+" ");
            System.out.println();
        }
    }

}
