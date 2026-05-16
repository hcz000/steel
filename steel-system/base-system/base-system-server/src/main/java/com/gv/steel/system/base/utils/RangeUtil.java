package com.gv.steel.system.base.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.gv.steel.common.core.util.MsgUtils;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class RangeUtil {
    public Map<Long, List<List<Range<BigDecimal>>>> hasIntersection(Map<Long, List<List<Range<BigDecimal>>>> compareRangeMap) {
        Map<Long, List<List<Range<BigDecimal>>>> hasIntersectionSpecRangeMap = Maps.newHashMap();

        for (Map.Entry<Long, List<List<Range<BigDecimal>>>> entry : compareRangeMap.entrySet()) {
            List<List<Range<BigDecimal>>> collectRangeList = entry.getValue();
            List<Range<BigDecimal>> firstRangeList = collectRangeList.get(0);
            if (firstRangeList.size() > 1) {
                // 通过冒泡，判断同材质的范围是否存在交集
                for (int i = 0; i < firstRangeList.size() - 1; i++) {
                    for (int j = i + 1; j < firstRangeList.size(); j++) {
                        final int one = i;
                        final int two = j;
                        if (collectRangeList.stream().allMatch(item -> item.get(one).isConnected(item.get(two)))) {
                            if (hasIntersectionSpecRangeMap.containsKey(entry.getKey())) {
                                for (int index = 0; index < collectRangeList.size(); index++) {
                                    hasIntersectionSpecRangeMap.get(entry.getKey()).get(index).add(collectRangeList.get(index).get(i));
                                    hasIntersectionSpecRangeMap.get(entry.getKey()).get(index).add(collectRangeList.get(index).get(j));
                                }
                            } else {
                                List<List<Range<BigDecimal>>> list = Lists.newArrayList();
                                for (List<Range<BigDecimal>> ranges : collectRangeList) {
                                    List<Range<BigDecimal>> arrayList = Lists.newArrayList(ranges.get(i), ranges.get(j));
                                    list.add(arrayList);
                                }
                                hasIntersectionSpecRangeMap.put(entry.getKey(), list);
                            }
                        }
                    }
                }
            }
        }

        return hasIntersectionSpecRangeMap;
    }

    public String hasIntersectionExceptionMsg(String properties, Map<Long, List<List<Range<BigDecimal>>>> hasIntersectionSpecRangeMap, Map<Long, String> materialNameMap) {
        StringBuilder msg = new StringBuilder();
        hasIntersectionSpecRangeMap.forEach((materialId, rangeList) -> {
            if (rangeList.stream().allMatch(CollUtil::isNotEmpty)) {
                List<String> rangeStrList = rangeList.stream().map(item -> item.stream()
                                .map(range -> ("[" + range.lowerEndpoint() + "~" + range.upperEndpoint() + "]"))
                                .collect(Collectors.joining(",")))
                        .collect(Collectors.toList());
                String[] propertiesParam = new String[rangeStrList.size() + 1];
                propertiesParam[0] = materialNameMap.get(materialId);
                for (int i = 0; i < rangeStrList.size(); i++) {
                    propertiesParam[i + 1] = rangeStrList.get(i);
                }
                msg.append(MsgUtils.getMessage(properties, propertiesParam)).append("\n");
            }
        });
        return msg.toString();
    }
}
