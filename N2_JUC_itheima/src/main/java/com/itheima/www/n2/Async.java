package com.itheima.www.n2;

import com.itheima.www.Constants;
import com.itheima.www.n2.util.FileReader;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步等待
 */
@Slf4j(topic = "c.Async")
public class Async {
    public static void main(String[] args) {
        // 异步读取
        new Thread(()-> FileReader.read(Constants.MP4_FULL_PATH)).start();
        log.info("do other things ...");
    }
}
