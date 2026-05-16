package com.gv.steel.common.log.util;

import cn.hutool.core.io.IoUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@UtilityClass
public class IPRegionUtil {
    private final static Searcher searcher;

    static {
        try {
            InputStream is = IPRegionUtil.class.getResourceAsStream("/ip2region.xdb");
            byte[] bytes = IoUtil.readBytes(is);
            searcher = Searcher.newWithBuffer(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRegion(String ip) {
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            log.error("search ip: [{}] region error: ", ip, e);
            return null;
        }
    }
}
