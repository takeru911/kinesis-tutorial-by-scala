package model

import java.io.IOException

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class StockTrade(
                val tickerSymbol: String,
                val tradeType: String,
                val price: Double,
                val quantity: Long,
                val id: Long
                ) {

  def toJsonAsBytes(): Array[Byte] = {
    try {
      return StockTrade.JSON.writeValueAsBytes(this)
    } catch {
      case e: IOException => null
    }
  }

  override def toString: String = {
    s"ID ${id}: ${tradeType} ${quantity} shares of ${tickerSymbol} for $$${price}"
  }
}

object StockTrade {
  private final val JSON = new ObjectMapper()
  JSON.registerModule(DefaultScalaModule)
  JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def fromJsonAsBytes(bytes: Array[Byte]): StockTrade = {
    try {
      return JSON.readValue(bytes, classOf[StockTrade])
    } catch {
      case e: IOException => return null
    }
  }
}
