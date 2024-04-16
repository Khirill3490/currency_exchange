package ru.skillbox.currency.exchange.entity.responce;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyResp {
    private String name;
    private Double value;
}
