package com.crio.warmup.stock.dto;

import java.sql.Date;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
// Implement the Candle interface in such a way that it matches the parameters returned
// inside Json response from Alphavantage service.

// Reference - https:www.baeldung.com/jackson-ignore-properties-on-serialization
// Reference - https:www.baeldung.com/jackson-name-of-property

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphavantageCandle implements Candle {

  @JsonProperty("1. open")
  private Double open;
  @JsonProperty("4. close")
  private Double close;
  @JsonProperty("2. high")
  private Double high;
  @JsonProperty("3. low")
  private Double low;
  
  private Date date;


  @Override
  public Double getOpen() {
    return open;
  }

  public void setOpen(Double open) {
    this.open = open;
  }

  @Override
  public Double getClose() {
    return close;
  }

  public void setClose(Double close) {
    this.close = close;
  }

  @Override
  public Double getHigh() {
    return high;
  }

  public void setHigh(Double high) {
    this.high = high;
  }

  @Override
  public Double getLow() {
    return low;
  }

  public void setLow(Double low) {
    this.low = low;
  }




  @Override
  public LocalDate getDate() {
    // return convertToLocalDateViaInstant(date); // Doesn't work
    return convertToLocalDateViaMilisecond(date); // Works
    // return convertToLocalDateViaSqlDate(date);
  }

  public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    // Source: https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }
  public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
  }
  public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
    return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
  }





  public void setDate(LocalDate timeStamp) {
    //this.date = convertToDateViaInstant(timeStamp); // -> Doesn't work
    this.date = convertToDateViaSqlDate(timeStamp);  // Works
  }

  public java.util.Date convertToDateViaInstant(LocalDate dateToConvert) {
    return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }
  public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
    return java.sql.Date.valueOf(dateToConvert);
  }
  




  @Override
  public String toString() {
    return "AlphavantageCandle{" + "open=" + open + ", close=" + close + ", high=" + high + ", low="
        + low + ", date=" + date + '}';
  }
}

