// package com.crio.warmup.stock.portfolio;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import java.time.LocalDate;
// import org.junit.jupiter.api.Test;
// import org.springframework.web.client.RestTemplate;

// // Purn .......
// @springBootTest
// public class PortfolioManagerImplTest {

//     @Test
//     void buildUriTest(){
//         String symbol = "AAPL";
//         LocalDate startDate = LocalDate.parse("2019-01-02");
//         LocalDate endDate = LocalDate.parse("2019-01-03");
//         String expected = "https://api.tiingo.com/tiingo/daily/AAPL/prices?startDate=2019-01-02&endDate=2019-01-03&token=8783737f8effeb1e3f239d886376dedd0a8d1630";
//         RestTemplate restTemplate = new RestTemplate();
//         PortfolioManagerImpl portfolioManagerImpl = new PortfolioManagerImpl( restTemplate );
//         String actual = portfolioManagerImpl.buildUri(symbol, startDate, endDate);
//         assertEquals(expected, actual);

//     }
    
// }
