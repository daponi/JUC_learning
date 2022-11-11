package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 练习：单词计数
 */
@Slf4j
public class TestWordCount {
    static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        // 生成测试数据
        // testALPHA();

        // 统计单词
        // test1();
        countWords(()->new ConcurrentHashMap<String , LongAdder>(),
                (countMap,wordsList)->{
                    for (String word : wordsList) {
                        /**
                         * computeIfAbsent()如果缺少一个Key，则计算生成一个value，然后将key value放入map，并返回旧vale
                         * 若是再用旧value+1则又不是线程安全的了，继续联想到了累加器Adder，进行旧值累加
                         * 循环中，map里同一个key里保存的是同一个累加器对象，所以累加的数据是正确的
                         *
                         * map里没有字母则存进map并将value置为adder对象，若有则返回其adder对象并+1
                         */
                        LongAdder counter = countMap.computeIfAbsent(word, (key) -> new LongAdder());
                        // map有单词则对该key锁队友的累加器adder进行累加
                        counter.increment();
                    }
                });
    }


    /**
     * 测试方法，开始统计单词；
     * 但发现不管用的HashMap还是ConcurrentHashMap都没有达到每个单词出现次数是200；
     * 这是因为临界区的存在，导致出现了线程安全问题，ConcurrentHashMap是线程安全的，但多个原子操作组合在一起就不一定线程安全
     * 若用synchronized加锁则正确但效率低，不能体现多线程访问的高效率
     */
    private static void test1() {
        countWords(
                // () -> new HashMap<String, Integer>(), //HashMap不是线程安全的
                () -> new ConcurrentHashMap<String,Integer>(),

                (countMap, wordsList) -> {
                    // 临界区

                    // synchronized (countMap) { //对整个保存结果的map加synchronize则效率低，不能体现26个线程访问的高效率
                        for (String word : wordsList) {
                            Integer number = countMap.get(word);
                            int newNumber = number == null ? 1 : number + 1;
                            countMap.put(word, newNumber);
                        }
                    // }
                });
    }

    /**
     * 启动26个线程给单词计数
     *
     * @param supplier 保存全部单词统计结果的Map
     * @param consumer 保存单词统计结果的Map，单个文本文件的读取内容
     * @param <T>      泛型
     */
    private static <T> void countWords(Supplier<Map<String, T>> supplier, BiConsumer<Map<String, T>, List<String>> consumer) {
        Map<String, T> countMap = supplier.get();
        List<Thread> ts = new ArrayList<>();
        long start =System.currentTimeMillis();

        for (int i = 0; i < 26; i++) {
            int index = i;
            Thread thread = new Thread(() -> {
                List<String> words = readFormFile(index + 1);
                consumer.accept(countMap, words);
            });
            ts.add(thread);
        }
        ts.forEach(Thread::start);
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end =System.currentTimeMillis();
        log.debug("统计结果:\n {}", countMap);
        log.debug("耗时：{}",end-start);

    }

    /**
     * 生成测试数据，放到26个文本里
     */
    private static void testALPHA() {
        int length = ALPHA.length();
        int count = 200;
        ArrayList<String> list = new ArrayList<>(length * count);//每个字母出现200次
        for (int i = 0; i < length; i++) {
            char ch = ALPHA.charAt(i);
            for (int j = 0; j < count; j++) {
                list.add(String.valueOf(ch));
            }
        }
        Collections.shuffle(list);//使用默认随机性源随机排列指定的列表

        // 将200*26个字母写到26个文本里，每行相当于一个单词
        for (int i = 0; i < 26; i++) {
            try (PrintWriter out = new PrintWriter(   //try块退出时，会自动调用()里的.close()方法，关闭资源，不用再写finally
                    new OutputStreamWriter(
                            new FileOutputStream("logs/" + (i + 1) + ".txt")))) {
                String collect = list.subList(i * count, (i + 1) * count).stream().collect(Collectors.joining("\n"));
                out.print(collect);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文本文件的单词
     *
     * @param i 第几个文本文件
     * @return
     */
    private static List<String> readFormFile(int i) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("logs/" + i + ".txt")));) {
            while (true) {
                String word = in.readLine();// 1次读1行相当于1个单词
                if (word == null) {
                    break;
                }
                words.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return words;
    }


}
