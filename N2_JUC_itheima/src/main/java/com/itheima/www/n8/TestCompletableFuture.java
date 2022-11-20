package com.itheima.www.n8;


import com.itheima.www.n2.util.Sleeper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 案例说明：电商比价需求，模拟如下情况：
 * <p>
 * 1需求：
 * 1.1 同一款产品，同时搜索出同款产品在各大电商平台的售价;
 * 1.2 同一款产品，同时搜索出本产品在同一个电商平台下，各个入驻卖家售价是多少
 * <p>
 * 2输出：出来结果希望是同款产品的在不同地方的价格清单列表，返回一个List<String>
 * 《mysql》 in jd price is 88.05
 * 《mysql》 in dangdang price is 86.11
 * 《mysql》 in taobao price is 90.43
 * <p>
 * 3 技术要求
 * 3.1 函数式编程
 * 3.2 链式编程
 * 3.3 Stream流式计算
 */
@Slf4j
public class TestCompletableFuture {
    // 需要查询的网商名
    static List<NetMall> list = Arrays.asList(
            new NetMall("jingdong"),
            new NetMall("taobao"),
            new NetMall("pdd"),
            new NetMall("dangdang"),
            new NetMall("tianmao")
    );

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<String> mysql = getPrice(list, "mysql");
        mysql.forEach(element->log.debug("{}",element));
        long end = System.currentTimeMillis();
        log.debug("cost time:{} 毫秒",end-start);

        log.debug("\n》》》》》》》》》》》》》分隔符《《《《《《《《《《《\n");

        long start2 = System.currentTimeMillis();
        List<String> mysql2 = getPriceByCompletableFuture(list, "mysql");
        mysql.forEach(element->log.debug("{}",element));
        long end2 = System.currentTimeMillis();
        log.debug("cost time:{} 毫秒",end2-start2);

    }

    /**
     * step by step 一家家串式搜查，组装输出结果
     * List<NetMall> ----->map------> List<String>
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPrice(List<NetMall> list, String productName) {
        //输出格式： 《mysql》 in taobao price is 90.43
        return list.stream()
                .map((netMall) ->
                        String.format(productName + " in %s price is %.2f", netMall.getNetMallName(), netMall.calcPrice(productName))
                ).collect(Collectors.toList());
    }


    /**
     * 使用CompletableFuture并行查询
     * List<NetMall> ----->List<CompletableFuture<String>>------> List<String>
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall>list,String productName){
        return list.stream().map(netMall ->
                CompletableFuture.supplyAsync(() -> String.format(productName + " in %s price is %.2f",
                        netMall.getNetMallName(),
                        netMall.calcPrice(productName))))
                .collect(Collectors.toList())
                .stream()
                .map(completableFuture -> completableFuture.join())
                .collect(Collectors.toList());
    }
}





/**
 * 模拟根据网商名称获取价格
 */
class NetMall {
    @Getter
    private String netMallName;

    public NetMall(String netMallName) {
        this.netMallName = netMallName;
    }

    public BigDecimal calcPrice(String productName) {
        Sleeper.sleep(1);
        return new BigDecimal(ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0));
    }
}