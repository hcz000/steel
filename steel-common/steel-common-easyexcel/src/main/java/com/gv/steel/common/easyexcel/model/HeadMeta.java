package com.gv.steel.common.easyexcel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * excel head meta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeadMeta implements Serializable {
    private static final long serialVersionUID = 1942549773644677476L;
    /**
     * excel head
     */
    private List<List<String>> head;
    /**
     * ignore head field
     */
    private Set<String> ignoreHeadFields;
}
