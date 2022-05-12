package com.itheima.www.n2;

import com.itheima.www.Constants;
import lombok.extern.slf4j.Slf4j;

import com.itheima.www.Constants;
import com.itheima.www.n2.util.FileReader;

/**
 * 同步等待
 */
@Slf4j(topic = "c.Sync")
public class Sync {

    public static void main(String[] args) {
        FileReader.read(Constants.MP4_FULL_PATH);
        log.info("do other things ...");
    }

}