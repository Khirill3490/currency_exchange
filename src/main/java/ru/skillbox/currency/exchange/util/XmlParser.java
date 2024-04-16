package ru.skillbox.currency.exchange.util;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.service.CurrencyService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Data
public class XmlParser {

    @Value("${url.xml}")
    private String urlFromConfig;
    private final CurrencyService currencyService;

    @Scheduled(fixedRate = 600_000) // 10 минут
    private void parseXml() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new URL(getUrlFromConfig()).openStream());
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Valute");
            List<Currency> currencyList = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element valuteElement = (Element) nodeList.item(i);
                Currency currency = new Currency();
                currency.setName(valuteElement.getElementsByTagName("Name")
                        .item(0).getTextContent());
                currency.setNominal(Long.parseLong(valuteElement.getElementsByTagName("Nominal")
                        .item(0).getTextContent()));
                currency.setValue(Double.valueOf(valuteElement.getElementsByTagName("Value")
                        .item(0).getTextContent().replaceAll(",", ".")));
                currency.setIsoNumCode(Long.parseLong(valuteElement.getElementsByTagName("NumCode")
                        .item(0).getTextContent()));
                currencyList.add(currency);
            }
            currencyService.addAll(currencyList);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
