package ru.skillbox.currency.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.currency.exchange.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByIsoNumCode(Long isoNumCode);
}
