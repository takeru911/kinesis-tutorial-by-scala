package processor

import java.util.UUID
import java.util.logging.Level
import java.util.logging.Logger

import com.amazonaws.regions.RegionUtils
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.{KinesisClientLibConfiguration, Worker}
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import utils.{ConfigurationUtils, CredentialUtils}

class StockTradesProcessor

object StockTradesProcessor {
  val LOG = LogFactory.getLog(classOf[StockTradesProcessor])
  val ROOT_LOGGER = Logger.getLogger("")
  val PROCESSOR_LOGGER = Logger.getLogger("processor")

  def checkUsage(args: Array[String]): Unit = {
    if(args.length != 3){
      System.err.println("使い方おかしいぞ")
      System.exit(1)
    }
  }

  def setLogLevels(): Unit = {
    ROOT_LOGGER.setLevel(Level.WARNING)
    PROCESSOR_LOGGER.setLevel(Level.INFO)
  }

  def main(args: Array[String]): Unit = {
    checkUsage(args)
    val applicationName = args(0)
    val streamName = args(1)
    val region = RegionUtils.getRegion(args(2))

    if(region == null) {
      System.err.println(args(2) + "is not a valid AWS region")
      System.exit(1)
    }

    setLogLevels()
    val credentialsProvider = CredentialUtils.getCredentialsProvider()
    val workerId = String.valueOf(UUID.randomUUID())
    val kclConfig = new KinesisClientLibConfiguration(applicationName, streamName, credentialsProvider, workerId)
      .withRegionName(region.getName)
      .withCommonClientConfig(ConfigurationUtils.getClientConfigWithUserAgent())
    val recordProcessorFactory = new StockTradeRecordProcessorFactory
    val worker = new Worker(recordProcessorFactory, kclConfig)
    var exitCode = 0
    try {
      worker.run()
    } catch {
      case t: Throwable => {
        LOG.error("Caught throable while processing data", t)
        exitCode = 1
      }
    }
    System.exit(exitCode)
  }
}
