package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.entity.responce.CurrencyResp;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;

    public List<CurrencyResp> findAll() {
        return repository.findAll().stream()
                .map(currency -> new CurrencyResp(currency.getName(), currency.getValue()))
                .collect(Collectors.toList());
    }



    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return  mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    public void addAll(List<Currency> currencyList) {
        List<Currency> listFromDb = repository.findAll();
        List<Currency> newList = new ArrayList<>();
        for (Currency c1: listFromDb) {
            for (Currency c2: currencyList) {
                if (c1.getName().equals(c2.getName())) {
                    c1.setValue(c2.getValue());
                    newList.add(c1);
                    currencyList.remove(c2);
                    break;
                }
            }
        }
        newList.addAll(currencyList);

        repository.deleteAll();
        repository.saveAll(newList);
    }
}
