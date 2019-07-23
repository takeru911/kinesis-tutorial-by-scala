package writer

import java.util.concurrent.atomic.AtomicLong

import model.{StockTrade}

import scala.util.Random


class StockTradeGenerator {
  private val MAX_DEVIATION = 0.2
  private val MAX_QUANTITY = 10000
  private val PROBABILITY_SELL = 0.4
  private val random = new Random()
  private var id = new AtomicLong(1)

  def getRandomTrade(): StockTrade = {
    val stockPrice = StockTradeGenerator.STOCK_PRICES(random.nextInt(StockTradeGenerator.STOCK_PRICES.size))
    val deviation = (random.nextDouble() - 0.5) * 2.0 * MAX_DEVIATION
    var price = stockPrice.price * (1 + deviation)
    price = Math.round(price * 100.0) / 100.0
    var tradeType = if(random.nextDouble() < PROBABILITY_SELL){
      "SELL"
    } else {
      "BUY"
    }
    val quantity = random.nextInt(MAX_QUANTITY) + 1
    new StockTrade(
      stockPrice.tickerSymbol,
      tradeType,
      price,
      quantity,
      id.getAndIncrement()
    )
  }
}



object StockTradeGenerator {
  private val STOCK_PRICES = List(
    new StockPrice("AAPL", 119.72),
    new StockPrice("XOM", 91.56),
    new StockPrice("GOOG", 527.83),
    new StockPrice("BRK.A", 223999.88),
    new StockPrice("MSFT", 42.36),
    new StockPrice("WFC", 54.21),
    new StockPrice("JNJ", 99.78),
    new StockPrice("WMT", 85.91),
    new StockPrice("CHL", 66.96),
    new StockPrice("GE", 24.64),
    new StockPrice("NVS", 102.46),
    new StockPrice("PG", 85.05),
    new StockPrice("JPM", 57.82),
    new StockPrice("RDS.A", 66.7),
    new StockPrice("CVX", 110.43),
    new StockPrice("PFE", 33.07),
    new StockPrice("FB", 74.44),
    new StockPrice("VZ", 49.09),
    new StockPrice("PTR", 111.08),
    new StockPrice("BUD", 120.39),
    new StockPrice("ORCL", 43.40),
    new StockPrice("KO", 41.23),
    new StockPrice("T", 34.64),
    new StockPrice("DIS", 101.73)
  )
}

class StockPrice(
                 val tickerSymbol: String,
                  val price: Double
                )

