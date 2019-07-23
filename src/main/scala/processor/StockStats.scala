package processor

import java.util

import model.{StockTrade}

class StockStats {
  val countsByTradeType: util.HashMap[String, util.HashMap[String, Long]] = new util.HashMap[String, util.HashMap[String, Long]]()
  val mostPopularByTradeType: util.HashMap[String, String] = new util.HashMap[String, String]()
  List("SELL", "BUY").foreach(
    countsByTradeType.put(_, new util.HashMap[String, Long]())
  )


  def addStockTrade(trade: StockTrade): Unit = {
    val tradeType = trade.tradeType
    val counts = countsByTradeType.get(tradeType)
    var count = counts.get(trade.tickerSymbol)
    if (count == null) {
      count = 0L
    }
    count += 1
    counts.put(trade.tickerSymbol, count)

    val mostPopular = mostPopularByTradeType.get(tradeType)
    if(mostPopular == null || countsByTradeType.get(tradeType).get(mostPopular) < count){
      mostPopularByTradeType.put(tradeType, trade.tickerSymbol)
    }
  }

  override def toString: String = {
    s"Most popular stock being bought: " + getMostPopularStock("BUY") + ", " + getMostPopularStockCount("BUY")
    "Most poplular stock being sold: " + getMostPopularStock("SELL") + ", " + getMostPopularStockCount("SELL")

  }

  def getMostPopularStock(tradeType: String): String = {
     mostPopularByTradeType.get(tradeType)
  }

  def getMostPopularStockCount(tradeType: String): Long = {
    val symbol = mostPopularByTradeType.get(tradeType)
    countsByTradeType.get(tradeType).get(symbol)
  }
}
