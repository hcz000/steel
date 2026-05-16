package com.gv.steel.system.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author SZB
 */
@Data
public class CurrencyPageDTO implements Serializable {

    private static final long serialVersionUID = 5742570525068658039L;

    private String name;

    private LocalDate[] timeZone;
}
