
package com.crio.warmup.stock.quotes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import com.crio.warmup.stock.PortfolioManagerApplication;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class TiingoServiceTest {

        @Mock
        private RestTemplate restTemplate;

        @Spy
        @InjectMocks
        private TiingoService tiingoService;

        // Purn testing if the below test will pass if the original getStockQuote is called: Comment
        // 1/2
        @Spy
        @InjectMocks
        private PortfolioManagerImpl portfolioManagerImpl;

        private String sampleTiingoResponse =
                        "[{\"date\":\"2019-01-02T00:00:00.000Z\",\"close\":1054.68,"
                                        + "\"high\":1060.79,\"low\":1025.28,\"open\":1027.2,\"volume\":1593395,\"adjClose\":1054.68,"
                                        + "\"adjHigh\":1060.79,\"adjLow\":1025.28,\"adjOpen\":1027.2,\""
                                        + "adjVolume\":1593395,\"divCash\""
                                        + ":0.0,\"splitFactor\":1.0},{\"date\":\"2019-01-03T00:00:00.000Z\","
                                        + "\"close\":1025.47,\"high\""
                                        + ":1066.26,\"low\":1022.37,\"open\":1050.67,\"volume\":2097957,\"adjClose\":1025.47,"
                                        + "\"adjHigh\":1066.26,\"adjLow\":1022.37,\"adjOpen\":1050.67,\"adjVolume\":2097957,"
                                        + "\"divCash\":0.0,\"splitFactor\":1.0},{\"date\":\"2019-01-04T00:00:00.000Z\","
                                        + "\"close\":1078.07,\"high\":1080.0,\"low\":1036.86,\"open\":1042.56,\"volume\":2301428,"
                                        + "\"adjClose\":1078.07,\"adjHigh\":1080.0,\"adjLow\":1036.86,\"adjOpen\":1042.56,\"adjVolume"
                                        + "\":2301428,\"divCash\":0.0,\"splitFactor\":1.0}]";

        @Test
        @MockitoSettings(strictness = Strictness.LENIENT)
        void getStockQuoteSingle() throws Exception {
                Mockito.doReturn(sampleTiingoResponse).when(restTemplate).getForObject(anyString(),
                                eq(String.class));

                List<Candle> candles = tiingoService.getStockQuote("GOOGL",
                                LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-04"));

                // Purn testing if the below test will pass if the original getStockQuote is called:
                // Comment 2/2
                // List<Candle> candles = portfolioManagerImpl.getStockQuote("GOOGL",
                // LocalDate.parse("2019-01-01"),
                // LocalDate.parse("2019-01-04"));

                assertEquals(candles.get(0).getOpen(), 1027.2, 0.3);

                ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
                Mockito.verify(restTemplate, atLeast(0)).getForObject(urlCaptor.capture(), any());

                ArgumentCaptor<String> urlCaptorWithMap = ArgumentCaptor.forClass(String.class);
                Mockito.verify(restTemplate, atLeast(0)).getForObject(urlCaptorWithMap.capture(),
                                any(), anyMap());

                ArgumentCaptor<URI> urlCaptorAsUri = ArgumentCaptor.forClass(URI.class);
                Mockito.verify(restTemplate, atLeast(0)).getForObject(urlCaptorAsUri.capture(),
                                any(Class.class));

                List<String> propertyKeyValues = urlCaptor.getAllValues();
                List<String> propertyKeyValues2 = urlCaptorWithMap.getAllValues();
                List<URI> propertyKeyValues3 = urlCaptorAsUri.getAllValues();

                assertTrue(!propertyKeyValues.isEmpty() || !propertyKeyValues2.isEmpty()
                                || !propertyKeyValues3.isEmpty());

        }

        // Additional test (PP): w/o using the Mockito part in the above test
        // @Test
        // void getStockQuoteSingleWOMockito() throws Exception {
        // // given
        // List<Candle> expected = null;
        // //List<Candle> expected = new ArrayList<>(Arrays.asList({213.43,
        // 214.01,214.5,212.38,"2010-01-04T00:00:00.000Z"}));
        // // when
        // List<Candle> actual = tiingoService.getStockQuote("AAPL",
        // LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-01"));

        // // then
        // Assertions.assertEquals(expected, actual);
        // }


        // @Test
        // @MockitoSettings(strictness = Strictness.LENIENT)
        // void purnTestingTiingoService() throws Exception {
        //         List<Candle> candles = tiingoService.getStockQuote("MFST",
        //                         LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-10"));
        //         Assert.isNonEmpty(candles);
        // }
}
