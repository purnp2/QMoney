
// package com.crio.warmup.stock;


// import com.crio.warmup.stock.dto.*;
// import com.crio.warmup.stock.log.UncaughtExceptionHandler;
// import com.crio.warmup.stock.portfolio.PortfolioManager;
// import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import java.io.File;
// import java.io.IOException;
// import java.net.URISyntaxException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.List;
// import java.util.UUID;
// import java.util.logging.Logger;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;
// import org.apache.logging.log4j.ThreadContext;
// import org.springframework.web.client.RestTemplate;


// public class PortfolioManagerApplication {

//   // TODO:..................... Module 1 ......................
//   // CRIO_TASK_MODULE_REST_API
//   // Find out the closing price of each stock on the end_date and return the list
//   // 3. Use RestTemplate#getForObject in order to call the API,
//   // and deserialize the results in List<Candle>

//   // args == [filename.json]
//   // Returns ["MSFT", "CSCO", "CTS"]
//   public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {
//     System.out.println("Array received by mainReadFile: " + Arrays.toString(args));
//     ObjectMapper objectMapper = getObjectMapper();
//     File fileNameAsFileObject = resolveFileFromResources(args[0]);
//     PortfolioTrade[] portfolioTrades =
//         objectMapper.readValue(fileNameAsFileObject, PortfolioTrade[].class);

//     ArrayList<String> stocksList = new ArrayList<String>();
//     for (PortfolioTrade portfolioTrade : portfolioTrades) {
//       stocksList.add(portfolioTrade.getSymbol());
//       System.out.println(portfolioTrade);
//     }
//     return stocksList;
//   }

//   private static void printJsonObject(Object object) throws IOException {
//     Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
//     ObjectMapper mapper = new ObjectMapper();
//     logger.info(mapper.writeValueAsString(object));
//   }

//   private static File resolveFileFromResources(String filename) throws URISyntaxException {
//     return Paths.get(Thread.currentThread().getContextClassLoader().getResource(filename).toURI())
//         .toFile();
//   }

//   private static ObjectMapper getObjectMapper() {
//     ObjectMapper objectMapper = new ObjectMapper();
//     objectMapper.registerModule(new JavaTimeModule());
//     return objectMapper;
//   }

//   public static List<String> debugOutputs() {
//     String valueOfArgument0 = "trades.json";
//     String resultOfResolveFilePathArgs0 =
//         "/home/crio-user/workspace/crio-786purn-ME_QMONEY_V2/qmoney/bin/main/trades.json";
//     String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@7c6908d7";
//     String functionNameFromTestFileInStackTrace = "PortfolioManagerApplicationTest.mainReadFile()";
//     String lineNumberFromTestFileInStackTrace = "29";

//     return Arrays.asList(
//         new String[] {valueOfArgument0, resultOfResolveFilePathArgs0, toStringOfObjectMapper,
//             functionNameFromTestFileInStackTrace, lineNumberFromTestFileInStackTrace});
//   }

//   // ..................... Module 2 ......................
//   /*
//    * In the mainReadQuotes() method: 1. Deserialize stock data from JSON file into a List of
//    * PortfolioTrade objects. 2. For each PortfolioTrade Object, a. Create the URI to be invoked,
//    * including the symbol, required state date and end date. b. Invoke the REST API for that URI
//    * using RestTemplate’s getForObject method and translate the response to a List of TiingoCandle
//    * objects. c. Get the closing price for that stock symbol from the TiingoCandle list, create a
//    * TotalReturnsDto object and append it to a List of TotalReturnsDto objects. 3. Write a
//    * comparator for TotalReturnsDto class to sort the stocks in ascending order of their closing
//    * prices. Invoke the comparator. 4. Create and return a List of Strings (from the sorted List of
//    * TotalReturnsDto) containing the sorted stock symbols.
//    */

//   // args == [filename.json, "2019-12-12"]
//   public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
//     System.out.println(
//         "ArrayList of fileName & endDate received by mainReadQuotes: " + Arrays.toString(args));
//     String fileNameJSON = args[0];
//     List<PortfolioTrade> portfolioTrades = readTradesFromJson(fileNameJSON);
//     LocalDate endDate = LocalDate.parse(args[1]);
//     String token = getToken();

//     // List<TotalReturnsDto> totalReturnsDtoList = buildTotalReturnsDtoList(portfolioTrades,
//     // endDate, token);
//     List<TotalReturnsDto> totalReturnsDtoList = new ArrayList<>();
//     for (PortfolioTrade portfolioTrade : portfolioTrades) {
//       List<Candle> candlesList = fetchCandles(portfolioTrade, endDate, token);
//       Double closingPriceOnEndDate = getClosingPriceOnEndDate(candlesList);
//       TotalReturnsDto totalReturnsDto =
//           new TotalReturnsDto(portfolioTrade.getSymbol(), closingPriceOnEndDate);
//       totalReturnsDtoList.add(totalReturnsDto);
//     }

//     // QUESTION: Because Java pass parameters as a value and NOT as a reference, am I sure the
//     // changes will
//     // reflect in currect method ??????????????
//     sortListByClosingPrice(totalReturnsDtoList);
//     List<String> extractedPortfolioSymbolsList = extractPortfolioSymbolsList(totalReturnsDtoList);
//     return extractedPortfolioSymbolsList;
//   }

//   public static String getToken() {
//     String token = "8783737f8effeb1e3f239d886376dedd0a8d1630";
//     return token;
//   }

//   public static List<String> extractPortfolioSymbolsList(
//       List<TotalReturnsDto> totalReturnsDtoList) {
//     List<String> sortedPortfolioTradeSymbolsList = new ArrayList<>();
//     for (TotalReturnsDto totalReturnsDto : totalReturnsDtoList) {
//       sortedPortfolioTradeSymbolsList.add(totalReturnsDto.getSymbol());
//     }
//     return sortedPortfolioTradeSymbolsList;
//   }

//   public static void sortListByClosingPrice(List<TotalReturnsDto> totalReturnsDtoList) {
//     System.out.println("For-loop complete, starting sorting using Comparator");
//     Collections.sort(totalReturnsDtoList, new Comparator<TotalReturnsDto>() {
//       @Override
//       public int compare(TotalReturnsDto o1, TotalReturnsDto o2) {
//         return (int) (o1.getClosingPrice().compareTo(o2.getClosingPrice()));
//       }
//     });
//   }

//   // TODO:
//   // After refactor, make sure that the tests pass by using these two commands
//   // ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
//   // ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
//   public static List<PortfolioTrade> readTradesFromJson(String filename)
//       throws IOException, URISyntaxException {
//     System.out.println("String received by readTradesFromJson: " + filename);
//     ObjectMapper objectMapper = getObjectMapper();
//     File fileNameAsFileObject = resolveFileFromResources(filename);
//     PortfolioTrade[] portfolioTrades =
//         objectMapper.readValue(fileNameAsFileObject, PortfolioTrade[].class);
//     return Arrays.asList(portfolioTrades);
//   }

//   // TODO:
//   // Build the Url using given parameters and use this function in your code to cann the API.
//   public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
//     String tradeSymbol = trade.getSymbol();
//     String purchaseDateString = trade.getPurchaseDate().toString();
//     String endDateString = endDate.toString();

//     // String url = "\'" +
//     // "https://api.tiingo.com/tiingo/daily/"+tradeSymbol+"/prices?startDate="+purchaseDateString+"&endDate="+endDateString+"&token="+token+"\'";
//     String url = "https://api.tiingo.com/tiingo/daily/" + tradeSymbol + "/prices?startDate="
//         + purchaseDateString + "&endDate=" + endDateString + "&token=" + token;
//     System.out.println("Returned URL: " + url);
//     return url;
//   }

//   // ..................... Module 3 ......................
//   // TODO:
//   // Ensure all tests are passing using below command
//   // ./gradlew test --tests ModuleThreeRefactorTest
//   static Double getOpeningPriceOnStartDate(List<Candle> candles) {
//     Candle firstCandle = candles.get(0);
//     return firstCandle.getOpen();
//   }

//   public static Double getClosingPriceOnEndDate(List<Candle> candles) {
//     Candle lastCandle = candles.get(candles.size() - 1);
//     return lastCandle.getClose();
//   }

//   public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
//     RestTemplate restTemplate = new RestTemplate();
//     String url = prepareUrl(trade, endDate, token);
//     TiingoCandle[] candlesArr = restTemplate.getForObject(url, TiingoCandle[].class);
//     List<Candle> candlesList = new ArrayList<Candle>(Arrays.asList(candlesArr));
//     return candlesList;
//   }

//   // TODO: CRIO_TASK_MODULE_CALCULATIONS
//   // Return the populated list of AnnualizedReturn for all stocks.
//   // Annualized returns should be calculated in two steps:
//   // 1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
//   // 1.1 Store the same as totalReturns
//   // 2. Calculate extrapolated annualized returns by scaling the same in years span.
//   // The formula is:
//   // annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
//   // 2.1 Store the same as annualized_returns
//   // Test the same using below specified command. The build should be successful.
//   // ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

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
//   // TODO: CRIO_TASK_MODULE_CALCULATIONS
//   // Now that you have the list of PortfolioTrade and their data, calculate annualized returns
//   // for the stocks provided in the Json.
//   // Use the function you just wrote #calculateAnnualizedReturns.
//   // Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

//   // Note:
//   // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
//   // 2. Remember to get the latest quotes from Tiingo API.

//   public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
//       throws IOException, URISyntaxException {
//     System.out.println(
//         "ArrayList of fileName & endDate received by mainCalculateSingleReturn: " + Arrays.toString(args));
//     String fileNameJSON = args[0];
//     List<PortfolioTrade> portfolioTrades = readTradesFromJson(fileNameJSON);
//     LocalDate endDate = LocalDate.parse(args[1]);
//     String token = getToken();
//     List<AnnualizedReturn> annualizedReturnsList = new ArrayList<>();

//     for (PortfolioTrade portfolioTrade : portfolioTrades) {
//       List<Candle> candlesList = fetchCandles(portfolioTrade, endDate, token);
//       Double openingPriceOnBuyDate = getOpeningPriceOnStartDate(candlesList); // == buyPrice
//       Double closingPriceOnEndDate = getClosingPriceOnEndDate(candlesList); // == sellPrice
//       AnnualizedReturn annualReturn = calculateAnnualizedReturns(endDate, portfolioTrade, openingPriceOnBuyDate, closingPriceOnEndDate);
//       //TotalReturnsDto totalReturnsDto = new TotalReturnsDto(portfolioTrade.getSymbol(), closingPriceOnEndDate);
//       annualizedReturnsList.add(annualReturn);
//     }

//     // QUESTION: Because Java pass parameters as a value and NOT as a reference, am I sure the
//     // changes will
//     // reflect in currect method ??????????????
//     sortListByAnnualizedReturnsDecreasing(annualizedReturnsList);
//     return annualizedReturnsList;



//   }

//   public static void sortListByAnnualizedReturnsDecreasing(List<AnnualizedReturn> annualizedReturnsList){
//     Collections.sort(annualizedReturnsList, new Comparator<AnnualizedReturn>(){
//       @Override
//       public int compare (AnnualizedReturn o1, AnnualizedReturn o2){
//         return (int) (-1)*(o1.getAnnualizedReturn().compareTo(o2.getAnnualizedReturn()));
//       }
//     });
//   }

//   // ..................... Module 4: Portfolio Management Library ......................
//   // TODO: CRIO_TASK_MODULE_REFACTOR
//   //  Once you are done with the implementation inside PortfolioManagerImpl and
//   //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
//   //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
//   //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

//   // Note:
//   // Remember to confirm that you are getting same results for annualized returns as in Module 3.

//   public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
//       throws Exception {
//        String file = args[0];
//        LocalDate endDate = LocalDate.parse(args[1]);
//        //String contents = readFileAsString(file);
//        ObjectMapper objectMapper = getObjectMapper();

//        File fileNameAsFileObject = resolveFileFromResources(file);
//        PortfolioTrade[] portfolioTrades =
//         objectMapper.readValue(fileNameAsFileObject, PortfolioTrade[].class);

//        RestTemplate restTemplate = new RestTemplate();
//        PortfolioManager portfolioManager = PortfolioManagerFactory.getPortfolioManager(restTemplate);
//        return portfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrades), endDate);
//   }


//   public static void main(String[] args) throws Exception {

//     Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
//     ThreadContext.put("runId", UUID.randomUUID().toString());

//     // Code HERE:

//     /*
//      * String fileName = "trades.json"; List<String> stocksAsList = new ArrayList<String>();
//      * String[] fileNameArray = {fileName}; stocksAsList = mainReadFile(fileNameArray);
//      * System.out.println(stocksAsList);
//      */
    
//     printJsonObject(mainReadFile(args));
//     printJsonObject(mainCalculateSingleReturn(args));
//     printJsonObject(mainCalculateReturnsAfterRefactor(args));
//   }
// }

package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;


public class PortfolioManagerApplication {
  
  public static RestTemplate restTemplate = new RestTemplate();
  public static PortfolioManager portfolioManager = PortfolioManagerFactory.getPortfolioManager(restTemplate);

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    File file=resolveFileFromResources(args[0]);

    ObjectMapper om=getObjectMapper();

    PortfolioTrade[] trade=om.readValue(file, PortfolioTrade[].class);
    
    List<String> ls= new ArrayList<String>(); 
    
    for(PortfolioTrade obj: trade){
      ls.add(obj.getSymbol());
    }

    return ls;
  }





  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.






  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/satyam-criodo-ME_QMONEY_V2/qmoney/bin/main/trades.json";
     String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@5542c4ed";
     String functionNameFromTestFileInStackTrace = "PortfolioManagerApplicationTest.mainReadFile()";
     String lineNumberFromTestFileInStackTrace = "29";


    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    List<TotalReturnsDto> totalReturnsDto = new ArrayList<TotalReturnsDto>();
    List<PortfolioTrade> portfolioTrade = readTradesFromJson(args[0]);
    for(PortfolioTrade t : portfolioTrade){
      LocalDate endDate = LocalDate.parse(args[1]);
      String url = prepareUrl(t, endDate, getToken());
      System.out.println(url);
      TiingoCandle[] results = restTemplate.getForObject(url, TiingoCandle[].class);
      if(results != null){
        totalReturnsDto.add(new TotalReturnsDto(t.getSymbol(), results[results.length - 1].getClose()));
      }
    }
    Collections.sort(totalReturnsDto, new Comparator<TotalReturnsDto>(){
      
      @Override
      public int compare(TotalReturnsDto o1, TotalReturnsDto o2){
        return (int) (o1.getClosingPrice().compareTo(o2.getClosingPrice()));
      }

    });

    List<String> listAnswer = new ArrayList<>();
    for(int i = 0; i < totalReturnsDto.size(); i++){
      listAnswer.add(totalReturnsDto.get(i).getSymbol());
    }
    return listAnswer;
  }

  public static String getToken(){
    String token1 = "bfab33eb91575e1b83114f8f2a19290f068cf05a";
    String token2 = "095a90b2f71f792debc98c73011d25dda41b387b";
    return token1;

  }
  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
    return candles.get(0).getOpen(); 
    
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
     return candles.get(candles.size()-1).getClose();
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
    RestTemplate obj = new RestTemplate();
    String url=prepareUrl(trade, endDate, token);
    TiingoCandle[] list =obj.getForObject(url, TiingoCandle[].class); 
    
    
    return Arrays.stream(list).collect(Collectors.toList());
  }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
      List<PortfolioTrade> portTrade=readTradesFromJson(args[0]);
      List<AnnualizedReturn> newList = new ArrayList<>();
      LocalDate date= LocalDate.parse(args[1]);

      for (PortfolioTrade temp: portTrade){
        List<Candle> candleList =fetchCandles(temp, date, getToken());
        AnnualizedReturn objNew=calculateAnnualizedReturns(date, temp, getOpeningPriceOnStartDate(candleList), getClosingPriceOnEndDate(candleList));
        newList.add(objNew);
      }

      Collections.sort(newList, new Comparator<AnnualizedReturn>() {

        @Override
        public int compare(AnnualizedReturn arg0, AnnualizedReturn arg1) {
          
          return (int) arg0.getAnnualizedReturn().compareTo(arg1.getAnnualizedReturn());
        }
        
      });

      Collections.reverse(newList);

     return newList;
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
      
        double totalReturn= (sellPrice-buyPrice)/buyPrice;
        double numOfYears= ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate) / 365.2422;
        double oneByNumOfYrs= 1.0/ numOfYears;
        double annualized_return = Math.pow((1.0+ totalReturn), oneByNumOfYrs) -1 ;
      
      
        return new AnnualizedReturn(trade.getSymbol(),annualized_return, totalReturn);
  }

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile

  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    File file = resolveFileFromResources(filename);
    ObjectMapper objectmapper = getObjectMapper();
    PortfolioTrade[] portfolioTrade = objectmapper.readValue(file, PortfolioTrade[].class);
    List<PortfolioTrade> listPortfolioTrade = Arrays.asList(portfolioTrade);
    return listPortfolioTrade;
  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    return  "https://api.tiingo.com/tiingo/daily/"+ trade.getSymbol() +"/prices?startDate="+ 
    trade.getPurchaseDate() +"&endDate="+ endDate +"&token=" + token;
  }


  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Once you are done with the implementation inside PortfolioManagerImpl and
  //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
  //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
  //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.

  public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
      throws Exception {
       String file = args[0];
       LocalDate endDate = LocalDate.parse(args[1]);
       String contents = readFileAsString(file);
       ObjectMapper objectMapper = getObjectMapper();
       PortfolioTrade[] portfolioTrade = objectMapper.readValue(file, PortfolioTrade[].class);
       return portfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrade), endDate);
  }

  private static String readFileAsString(String fileName) throws IOException, URISyntaxException {
    return new String(Files.readAllBytes(resolveFileFromResources(fileName).toPath()), "UTF-8");
  }



  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    printJsonObject(mainCalculateReturnsAfterRefactor(args));
  }
}


