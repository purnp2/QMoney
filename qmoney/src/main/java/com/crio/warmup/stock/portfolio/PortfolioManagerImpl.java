
// package com.crio.warmup.stock.portfolio;

// import static java.time.temporal.ChronoUnit.DAYS;
// import static java.time.temporal.ChronoUnit.SECONDS;

// import com.crio.warmup.stock.dto.AnnualizedReturn;
// import com.crio.warmup.stock.dto.Candle;
// import com.crio.warmup.stock.dto.PortfolioTrade;
// import com.crio.warmup.stock.dto.TiingoCandle;
// import com.crio.warmup.stock.exception.StockQuoteServiceException;
// import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
// import com.crio.warmup.stock.quotes.StockQuotesService;
// import com.crio.warmup.stock.quotes.TiingoService;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.List;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.Collectors;
// import org.springframework.web.client.RestTemplate;

// public class PortfolioManagerImpl implements PortfolioManager {

//   RestTemplate restTemplate;
//   String provider = null;

//   // Caution: Do not delete or modify the constructor, or else your build will break!
//   // This is absolutely necessary for backward compatibility
//   protected PortfolioManagerImpl(RestTemplate restTemplate) {
//     this.restTemplate = restTemplate;
//   }

//   protected PortfolioManagerImpl(String provider, RestTemplate restTemplate) {
//     this.restTemplate = restTemplate;
//     this.provider = provider;
//   }

//   StockQuotesService stockQuotesService = StockQuoteServiceFactory.getService(provider, restTemplate);

//   // TODO: CRIO_TASK_MODULE_REFACTOR
//   // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
//   // Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
//   // into #calculateAnnualizedReturn function here and ensure it follows the method signature.
//   // 2. Logic to read Json file and convert them into Objects will not be required further as our
//   // clients will take care of it, going forward.

//   // Note:
//   // Make sure to exercise the tests inside PortfolioManagerTest using command below:
//   // ./gradlew test --tests PortfolioManagerTest

//   // CHECKSTYLE:OFF



//   // CHECKSTYLE:OFF

//   // TODO: CRIO_TASK_MODULE_REFACTOR
//   // Extract the logic to call Tiingo third-party APIs to a separate function.
//   // Remember to fill out the buildUri function and use that.


//   // Purn's original implementation which was commented out by Satyam sir becuase it was causing old
//   // unit tests to fail.
//   // public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
//   // LocalDate endDate) throws JsonProcessingException {
//   // // String token = getToken();
//   // List<AnnualizedReturn> annualizedReturnsList = new ArrayList<>();

//   // for (PortfolioTrade portfolioTrade : portfolioTrades) {
//   // String symbol = portfolioTrade.getSymbol();
//   // LocalDate purchaseDate = portfolioTrade.getPurchaseDate();

//   // // StockQuoteServiceFactory stockQuoteServiceFactory = new stockQuoteServiceFactory();
//   // // The above new-operator is not working. I guess because this factory returns ENUM class.
//   // stockQuotesService = StockQuoteServiceFactory.getService(provider, restTemplate);

//   // // List<Candle> candlesList = new TiingoService.getStockQuote(symbol, purchaseDate, endDate);
//   // List<Candle> candlesList = stockQuotesService.getStockQuote(symbol, purchaseDate, endDate);

//   // Double openingPriceOnBuyDate = getOpeningPriceOnStartDate(candlesList); // == buyPrice
//   // Double closingPriceOnEndDate = getClosingPriceOnEndDate(candlesList); // == sellPrice
//   // AnnualizedReturn annualReturn = calculateAnnualizedReturns(endDate, portfolioTrade,
//   // openingPriceOnBuyDate, closingPriceOnEndDate);
//   // // TotalReturnsDto totalReturnsDto = new TotalReturnsDto(portfolioTrade.getSymbol(),
//   // // closingPriceOnEndDate);
//   // annualizedReturnsList.add(annualReturn);
//   // }
//   // // QUESTION @ PP: Because Java pass parameters as a value and NOT as a reference, am I sure the
//   // // changes will reflect in currect method ?
//   // sortListByAnnualizedReturnsDecreasing(annualizedReturnsList);
//   // return annualizedReturnsList;
//   // }


//   // Ticket #80858: code refactored by Satyam sir to resolve unit test failing.

//   public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
//       LocalDate endDate) throws StockQuoteServiceException {
//     List<AnnualizedReturn> annualizedReturns = new ArrayList<>();

//     for (PortfolioTrade portfolioTrade : portfolioTrades) {
//       List<Candle> candles;
//       try {
//         //@Deprecated
//         // candles =
//         //     getStockQuote(portfolioTrade.getSymbol(), portfolioTrade.getPurchaseDate(), endDate);

//         candles = 
//             stockQuotesService.getStockQuote(portfolioTrade.getSymbol(), portfolioTrade.getPurchaseDate(), endDate);
//         AnnualizedReturn annualizedReturn = calculateAnnualizedReturns(endDate, portfolioTrade,
//             getOpeningPriceOnStartDate(candles), getClosingPriceOnEndDate(candles));
//         annualizedReturns.add(annualizedReturn);
//       } catch (JsonProcessingException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//       }
//     }

//     return annualizedReturns.stream().sorted(getComparator()).collect(Collectors.toList());
//   }



//   // public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
//   //   RestTemplate restTemplate = new RestTemplate();
//   //   String url = prepareUrl(trade, endDate, token);
//   //   TiingoCandle[] candlesArr = restTemplate.getForObject(url, TiingoCandle[].class);
//   //   List<Candle> candlesList = new ArrayList<Candle>(Arrays.asList(candlesArr));
//   //   return candlesList;
//   // }

//   // public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
//   //   String tradeSymbol = trade.getSymbol();
//   //   String purchaseDateString = trade.getPurchaseDate().toString();
//   //   String endDateString = endDate.toString();
//   //   String url = "https://api.tiingo.com/tiingo/daily/" + tradeSymbol + "/prices?startDate="
//   //       + purchaseDateString + "&endDate=" + endDateString + "&token=" + token;
//   //   System.out.println("Returned URL: " + url);
//   //   return url;
//   // }

  
//   @Deprecated
//   public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
//       throws JsonProcessingException {
//     //RestTemplate restTemplate = new RestTemplate();
//     // String url = prepareUrl(trade, endDate, token);
//     String url = buildUri(symbol, from, to);
//     TiingoCandle[] candlesArr = restTemplate.getForObject(url, TiingoCandle[].class);
//     List<Candle> candlesList = new ArrayList<Candle>(Arrays.asList(candlesArr));
//     return candlesList;
//   }

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


//   static Double getOpeningPriceOnStartDate(List<Candle> candles) {
//     Candle firstCandle = candles.get(0);
//     return firstCandle.getOpen();
//   }

//   public static Double getClosingPriceOnEndDate(List<Candle> candles) {
//     Candle lastCandle = candles.get(candles.size() - 1);
//     return lastCandle.getClose();
//   }

//   public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate, PortfolioTrade trade,
//       Double buyPrice, Double sellPrice) {
//     Double totalReturns = getHoldingPeriodReturn(buyPrice, sellPrice);
//     Double total_num_days = (double) ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
//     Double total_num_years = total_num_days / 364.24;
//     Double annualized_returns = Math.pow((1 + totalReturns), (1 / total_num_years)) - 1.0;
//     return new AnnualizedReturn(trade.getSymbol(), annualized_returns, totalReturns);
//   }

//   public static Double getHoldingPeriodReturn(Double buyPrice, Double sellPrice) {
//     Double holdingPeriodReturn = (sellPrice - buyPrice) / buyPrice;
//     return holdingPeriodReturn;
//   }

//   // public static void sortListByAnnualizedReturnsDecreasing2(
//   // List<AnnualizedReturn> annualizedReturnsList) {
//   // Collections.sort(annualizedReturnsList, new Comparator<AnnualizedReturn>() {
//   // @Override
//   // public int compare(AnnualizedReturn o1, AnnualizedReturn o2) {
//   // return (int) (-1) * (o1.getAnnualizedReturn().compareTo(o2.getAnnualizedReturn()));
//   // }
//   // });
//   // }

//   public static void sortListByAnnualizedReturnsDecreasing(
//       List<AnnualizedReturn> annualizedReturnsList) {
//     // Comparator<AnnualizedReturn> comp = new getComparator();
//     // Collections.sort(annualizedReturnsList, comp );
//     Collections.sort(annualizedReturnsList, getComparator());
//   }

//   // PP changed this getComparator method to static. Is this okey?
//   private static Comparator<AnnualizedReturn> getComparator() {
//     return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
//   }

//   /*
//    * private Comparator<AnnualizedReturn> getComparator() { return
//    * Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed(); }
//    */

//   // ¶TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
//   // Modify the function #getStockQuote and start delegating to calls to
//   // stockQuoteService provided via newly added constructor of the class.
//   // You also have a liberty to completely get rid of that function itself, however, make sure
//   // that you do not delete the #getStockQuote function.

// }

package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {


  protected RestTemplate restTemplate= new RestTemplate();
  StockQuotesService stockQuotesService;


  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
  }

  public PortfolioManagerImpl(){

  }


  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF




  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws StockQuoteServiceException, JsonProcessingException {
    //      String url= buildUri(symbol, from , to);
     
    //  TiingoCandle[] tiingoresult=restTemplate.getForObject(url, TiingoCandle[].class);
      
    //  if(tiingoresult!=null)
    //       return Arrays.stream(tiingoresult).collect(Collectors.toList());
    //  return new ArrayList<Candle>();

    return stockQuotesService.getStockQuote(symbol, from, to);
  }


  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = "https:api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
            + "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";
      return uriTemplate;
  }



  public String getToken(){
    String token1 = "bfab33eb91575e1b83114f8f2a19290f068cf05a";
    String token2 = "095a90b2f71f792debc98c73011d25dda41b387b";
    return token1;
  }

  // protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
  //   return "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?startDate=" + startDate 
  //   + "&endDate=" + endDate + "&token=" + getToken();
  // }


  private Double getOpeningPriceOnStartDate(List<Candle> candles) {
    return candles.get(0).getOpen();
  }

  private Double getClosingPriceOnEndDate(List<Candle> candles) {
    return candles.get(candles.size() - 1).getClose();
  }


  private AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate, PortfolioTrade trade,
      Double buyPrice, Double sellPrice) {
    double total_num_years = DAYS.between(trade.getPurchaseDate(), endDate) / 365.2422;
    double totalReturns = (sellPrice - buyPrice) / buyPrice;
    double annualized_returns = Math.pow((1.0 + totalReturns), (1.0 / total_num_years)) - 1;
    return new AnnualizedReturn(trade.getSymbol(), annualized_returns, totalReturns);
  }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades, LocalDate endDate) throws StockQuoteServiceException 
  {
    List<AnnualizedReturn> annualizedReturns = new ArrayList<>();

    for(PortfolioTrade pft:portfolioTrades){

      List<Candle> candleOutputList;
      try {
        candleOutputList = getStockQuote(pft.getSymbol(), pft.getPurchaseDate(), endDate);
        AnnualizedReturn anualizedOutput= calculateAnnualizedReturns(endDate,pft,getOpeningPriceOnStartDate(candleOutputList),getClosingPriceOnEndDate(candleOutputList));          
        annualizedReturns.add(anualizedOutput);

      } catch (JsonProcessingException e) {
        // e.printStackTrace();
        throw new StockQuoteServiceException(e.getMessage());
       
      }
      //  catch (StockQuoteServiceException e) {
        
      //   throw new StockQuoteServiceException(e.getMessage());
      // }

   
  }
  return annualizedReturns.stream().sorted(getComparator()).collect(Collectors.toList());
}  

}

