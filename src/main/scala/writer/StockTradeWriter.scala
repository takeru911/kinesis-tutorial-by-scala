package writer

import java.nio.ByteBuffer

import com.amazonaws.AmazonClientException
import com.amazonaws.regions.{Region, RegionUtils}
import org.apache.commons.logging.LogFactory
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.kinesis.model.{PutRecordRequest, ResourceNotFoundException}
import com.amazonaws.services.kinesis.{AmazonKinesis, AmazonKinesisClient, AmazonKinesisClientBuilder}
import model.StockTrade
import utils.{ConfigurationUtils, CredentialUtils}

class StockTradeWriter{}

object StockTradeWriter {
  val LOG = LogFactory.getLog(classOf[StockTradeWriter])

  private def checkUsage(args: Array[String]): Unit = {
    if(args.length != 2){
      System.err.println("Usage: $0 <stream name> <region>")
      System.exit(1)
    }
  }

  private def validateString(kinesisClient: AmazonKinesis, streamName: String): Unit = {
    try {
      val result = kinesisClient.describeStream(streamName)

      if("ACTIVE" != result.getStreamDescription.getStreamStatus){
        System.err.println(s"Stream $streamName is not active. please wait a few moments and try again.")
        System.exit(1)
      }
      LOG.info("valide stream")
    } catch {
      case e: ResourceNotFoundException => {
          System.err.println(s"Stream $streamName does not exist")
          System.err.println(e)
          System.exit(1)
        }
      case e: Exception => {
          System.err.println(s"Error found while describing the stream $streamName")
          System.err.println(e)
          System.exit(1)
        }
    }

  }

  private def buildKinesisClient(regionName: String): AmazonKinesis = {
    val credentials = CredentialUtils.getCredentialsProvider()
    val kinesisClientBuilder = AmazonKinesisClientBuilder.standard()
    kinesisClientBuilder.setCredentials(credentials)
    kinesisClientBuilder.setClientConfiguration(ConfigurationUtils.getClientConfigWithUserAgent())
    kinesisClientBuilder.setRegion(regionName)
    kinesisClientBuilder.build()
  }

  private def sendStockTrade(trade: StockTrade, kinesisClient: AmazonKinesis, streamName: String): Unit = {
    val bytes = trade.toJsonAsBytes()
    if (bytes == null) {
      LOG.warn("Could not get JSON bytes for stock trade")
      return
    }
    LOG.info("Putting trade" + trade)
    val putRecord = new PutRecordRequest
    putRecord.setStreamName(streamName)
    putRecord.setPartitionKey(trade.tickerSymbol)
    putRecord.setData(ByteBuffer.wrap(bytes))

    try{
      kinesisClient.putRecord(putRecord)
    } catch {
      case e: AmazonClientException => LOG.warn("Error sending recor Amazon kinesis" + e)
    }
  }

  def main(args: Array[String]): Unit = {
    checkUsage(args)

    val streamName = args(0)
    val regionName = args(1)

    val region = RegionUtils.getRegion(regionName)
    if (region == null) {
      System.err.println(s"$regionName is not a valid AWS Region")
      System.exit(1)
    }
    val kinesisClient = buildKinesisClient(regionName)
    validateString(kinesisClient, streamName)
    val stockTradeGenerator = new StockTradeGenerator
    while(true) {
      val trade = stockTradeGenerator.getRandomTrade()
      sendStockTrade(trade, kinesisClient, streamName)
      Thread.sleep(100)
    }
  }


}
