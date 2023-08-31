
// package com.crio.warmup.stock.quotes;

// import static java.time.temporal.ChronoUnit.DAYS;
// import static java.time.temporal.ChronoUnit.SECONDS;
// import com.crio.warmup.stock.dto.AlphavantageCandle;
// import com.crio.warmup.stock.dto.AlphavantageDailyResponse;
// import com.crio.warmup.stock.dto.Candle;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
// import java.util.stream.Collectors;
// import org.springframework.util.SerializationUtils;
// import org.springframework.web.client.RestTemplate;

// public class AlphavantageService implements StockQuotesService {

// // Purn
// protected RestTemplate restTemplate;

// protected AlphavantageService(RestTemplate restTemplate) {
// this.restTemplate = restTemplate;
// }

// // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
// // Implement the StockQuoteService interface as per the contracts. Call Alphavantage service
// // to fetch daily adjusted data for last 20 years.
// // Refer to documentation here: https://www.alphavantage.co/documentation/
// // --
// // The implementation of this functions will be doing following tasks:
// // 1. Build the appropriate url to communicate with third-party.
// // The url should consider startDate and endDate if it is supported by the provider.
// // 2. Perform third-party communication with the url prepared in step#1
// // 3. Map the response and convert the same to List<Candle>
// // 4. If the provider does not support startDate and endDate, then the implementation
// // should also filter the dates based on startDate and endDate. Make sure that
// // result contains the records for for startDate and endDate after filtering.
// // 5. Return a sorted List<Candle> sorted ascending based on Candle#getDate
// // IMP: Do remember to write readable and maintainable code, There will be few functions like
// // Checking if given date falls within provided date range, etc.
// // Make sure that you write Unit tests for all such functions.
// // Note:
// // 1. Make sure you use {RestTemplate#getForObject(URI, String)} else the test will fail.
// // 2. Run the tests using command below and make sure it passes:
// // ./gradlew test --tests AlphavantageServiceTest
// // CHECKSTYLE:OFF
// // CHECKSTYLE:ON
// // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
// // 1. Write a method to create appropriate url to call Alphavantage service. The method should
// // be using configurations provided in the {@link @application.properties}.
// // 2. Use this method in #getStockQuote.

// public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
// throws JsonProcessingException {

// String response = restTemplate.getForObject(buildUri(symbol), String.class);
// ObjectMapper objectMapper = new ObjectMapper();
// objectMapper.registerModule(new JavaTimeModule());
// // AlphavantageDailyResponse AVCandleMap = new AlphavantageDailyResponse();
// List<Candle> candlesList = new ArrayList<>();
// try {
// AlphavantageDailyResponse avDailyResponseObject =
// objectMapper.readValue(response, AlphavantageDailyResponse.class);
// candlesList = filterFromToToDatesAndChangeMapToCandlesList(avDailyResponseObject, from, to);
// } catch (Exception ex) {
// ex.printStackTrace();
// throw ex;
// // return candlesList;
// }
// return candlesList;
// }

// private List<Candle> filterFromToToDatesAndChangeMapToCandlesList(
// AlphavantageDailyResponse avDailyResponseObject, LocalDate from, LocalDate to) {

// Map<LocalDate, AlphavantageCandle> avDailyResponseMap = avDailyResponseObject.getCandles();
// List<Candle> candlesList = new ArrayList<Candle>();
// AlphavantageCandle tempCandle;


// for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
// if (avDailyResponseMap.containsKey(date)) {
// // Add the Date (after converting it from LocalDate) into AlphaVantageCandle because
// // it is missing there by default after Jackson ObjectMapper parsing.
// // The change from LocalDate to Date is defined in #AlphavantageCandle.java
// // avDailyResponseMap.get(x).setDate(x); -> This will cause ConcurrentModificationException.
// tempCandle = avDailyResponseMap.get(date);
// tempCandle.setDate(date);
// candlesList.add(tempCandle);
// }
// }

// return candlesList;
// }

// // CHECKSTYLE:OFF

// // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
// // Write a method to create appropriate url to call the Tiingo API.

// protected String buildUri(String symbol) {
// String apiKey = getToken();
// String uriTemplate =
// "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + symbol
// + "&outputsize=full&apikey=" + apiKey;
// System.out.println("Returned uriTemplate: " + uriTemplate);
// return uriTemplate;
// }

// private String getToken() {
// String token = "BPY9P2MIDNZWCMCM";
// return token;
// }

// }


package com.crio.warmup.stock.quotes;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import com.crio.warmup.stock.dto.AlphavantageCandle;
import com.crio.warmup.stock.dto.AlphavantageDailyResponse;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class AlphavantageService implements StockQuotesService {



  RestTemplate restTemplate = new RestTemplate();
  AlphavantageDailyResponse alphadailyres;

  public AlphavantageService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  String getAPI() {
    String token = "BPY9P2MIDNZWCMCM"; //Purn
    String token2 = "0K4LCXEQZW4RSEE6"; // Crio's sir added
    return token;
  }

  String createUrl(String symbol) {

    return "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + symbol
        + "&outputsize=full&apikey=" + getAPI();
  }

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws StockQuoteServiceException {


    String responseString = restTemplate.getForObject(createUrl(symbol), String.class);
    // System.out.println(responseString);

    AlphavantageDailyResponse alphavantageDailyResponse = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());

      alphavantageDailyResponse =
          objectMapper.readValue(responseString, AlphavantageDailyResponse.class);
      if (alphavantageDailyResponse.getCandles() == null || responseString == null)
        // try {
          throw new StockQuoteServiceException("Invalid Response Found");
          
    } catch (JsonProcessingException e) {
     
        throw new StockQuoteServiceException(e.getMessage());
    }

    List<Candle> alphavantageCandles = new ArrayList<>();
    Map<LocalDate, AlphavantageCandle> mapOFDateAndAlphavantageCandle =
        alphavantageDailyResponse.getCandles();
    for (LocalDate localDate : mapOFDateAndAlphavantageCandle.keySet()) {
      if (localDate.isAfter(from.minusDays(1)) && localDate.isBefore(to.plusDays(1))) {
        AlphavantageCandle alphavantageCandle =
            alphavantageDailyResponse.getCandles().get(localDate);
        alphavantageCandle.setDate(localDate);
        alphavantageCandles.add(alphavantageCandle);
      }
    }
    return alphavantageCandles.stream().sorted(Comparator.comparing(Candle::getDate))
        .collect(Collectors.toList());

  }
}

