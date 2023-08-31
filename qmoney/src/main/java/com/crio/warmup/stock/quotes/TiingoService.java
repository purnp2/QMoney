
// package com.crio.warmup.stock.quotes;

// import com.crio.warmup.stock.dto.Candle;
// import com.crio.warmup.stock.dto.TiingoCandle;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;
// import org.springframework.web.client.RestTemplate;

// public class TiingoService implements StockQuotesService {
//   protected RestTemplate restTemplate;


//   protected TiingoService(RestTemplate restTemplate) {
//     this.restTemplate = restTemplate;
//   }


//   // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
//   // Implement getStockQuote method below that was also declared in the interface.

//   // Note:
//   // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
//   // 2. Run the tests using command below and make sure it passes.
//   // ./gradlew test --tests TiingoServiceTest
  
//   public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws JsonProcessingException
//   {
//     // QUESTION: The test will fail if I don't intialize the below new restTemplate once again this funtion, ...
//     // despite it has already been initialized once for this class.
//     // RestTemplate restTemplate = new RestTemplate();
//     /* String url = buildUri(symbol, from, to);
//     List<Candle> candlesList = Collections.emptyList();
//     try{
//     TiingoCandle[] candlesArr = restTemplate.getForObject(url, TiingoCandle[].class);
//     candlesList = new ArrayList<Candle>(Arrays.asList(candlesArr));
//     } catch (IndexOutOfBoundsException | NullPointerException e){
//       return candlesList;
//     }
//     return candlesList; */
//     String response = restTemplate.getForObject(buildUri(symbol, from, to), String.class);
//     ObjectMapper objectMapper = new ObjectMapper();
//     objectMapper.registerModule(new JavaTimeModule());
//     Candle[] result = objectMapper.readValue(response, TiingoCandle[].class);
//     return Arrays.asList(result);

//   }

//   // CHECKSTYLE:OFF

//   // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
//   // Write a method to create appropriate url to call the Tiingo API.

//   protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
//     String apiKey = getToken();
//     String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?" + "startDate="
//         + startDate + "&endDate=" + endDate + "&token=" + apiKey;
//     System.out.println("Returned uriTemplate: " + uriTemplate);
//     return uriTemplate;
//   }
  
//   public static String getToken() {
//     String token = "8783737f8effeb1e3f239d886376dedd0a8d1630";
//     return token;
//   }

// }


package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  RestTemplate restTemplate=new RestTemplate();

  public String getToken(){
    String token0 = "8783737f8effeb1e3f239d886376dedd0a8d1630"; // Purn main
    String token1 = "bfab33eb91575e1b83114f8f2a19290f068cf05a"; // Crio's help
    String token2 = "095a90b2f71f792debc98c73011d25dda41b387b"; // Purn's second
    return token0;

  }

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  protected String buildURL(String symbol, LocalDate startDate, LocalDate endDate) {

    String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?" + "startDate="
        + startDate + "&endDate=" + endDate + "&token=" + getToken();
    return uriTemplate;
  }


  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws StockQuoteServiceException
      {
        ObjectMapper objmapper=new ObjectMapper();
        objmapper.registerModule(new JavaTimeModule());

        Candle[] candleobj=null;
        
        // try{
        
        // } catch (StockQuoteServiceException e){
        //   e.printStackTrace();
        // }

        try {

          String apiresponse=restTemplate.getForObject(buildURL(symbol, from, to), String.class);
          candleobj=objmapper.readValue(apiresponse, TiingoCandle[].class);
          if(candleobj==null || apiresponse==null){
            throw new StockQuoteServiceException("Tiingo Service Invalid Return");
          }
    
        } catch (JsonMappingException e) {
          // TODO Auto-generated catch block
          throw new StockQuoteServiceException(e.getMessage());
          // e.printStackTrace();
        } catch (JsonProcessingException e1) {
          // TODO Auto-generated catch block
          // e1.printStackTrace();
          throw new StockQuoteServiceException(e1.getMessage());
        }
        
        return Arrays.asList(candleobj);
      }

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

}
