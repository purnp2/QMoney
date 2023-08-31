package com.crio.warmup.stock.dto;

import java.util.List;

public class TiingoCandleList {
    private List<TiingoCandle> tiingoCandleList;

    public List<TiingoCandle> getTiingoCandleList(){
        return this.tiingoCandleList;
    }

    public static TiingoCandle get(int i) {
        return TiingoCandleList.get(i);
    }

    public void setTiingoCandleList(List<TiingoCandle> tiingoCandleList){
        this.tiingoCandleList = tiingoCandleList;
    }
}
