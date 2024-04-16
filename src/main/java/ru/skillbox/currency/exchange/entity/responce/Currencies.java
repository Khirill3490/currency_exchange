package ru.skillbox.currency.exchange.entity.responce;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Currencies {
    List<CurrencyResp> currencies;
}
