package processor

import java.util

import com.amazonaws.services.kinesis.clientlibrary.exceptions.{InvalidStateException, ShutdownException, ThrottlingException}
import com.amazonaws.services.kinesis.clientlibrary.interfaces.{IRecordProcessor, IRecordProcessorCheckpointer}
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason
import com.amazonaws.services.kinesis.model.Record
import model.StockTrade
import org.apache.commons.logging.LogFactory

class StockTradeRecordProcessor extends IRecordProcessor{
  var kinesisShardId: String = ""
  var nextReportingTimeInMillis: Long = 0
  var nextCheckpointTimeInMillis: Long = 0
  var stockStats = new StockStats()

  override def initialize(shardId: String): Unit = {
    StockTradeRecordProcessor.LOG.info(s"Initializing record processor for shard: $shardId")
    this.kinesisShardId = shardId
    nextReportingTimeInMillis = System.currentTimeMillis() + StockTradeRecordProcessor.REPORTING_INTERVAL_MILLIS
    nextCheckpointTimeInMillis = System.currentTimeMillis() + StockTradeRecordProcessor.CHECKPOINT_INTERVAL_MILLIS
  }

  override def processRecords(records: util.List[Record], checkpointer: IRecordProcessorCheckpointer): Unit = {
    records.forEach(
      processRecord(_)
    )

    if(System.currentTimeMillis() > nextReportingTimeInMillis){
      reportStats()
      resetStats()
      nextReportingTimeInMillis = System.currentTimeMillis() + StockTradeRecordProcessor.REPORTING_INTERVAL_MILLIS
    }

    if(System.currentTimeMillis() > nextCheckpointTimeInMillis) {
      checkpoint(checkpointer)
      nextCheckpointTimeInMillis = System.currentTimeMillis() + StockTradeRecordProcessor.CHECKPOINT_INTERVAL_MILLIS
    }
  }

  private def processRecord(record: Record): Unit = {
    val trade = StockTrade.fromJsonAsBytes(record.getData.array())

    if(trade == null) {
      StockTradeRecordProcessor.LOG.warn("Skipping record. Unable to parse record into StockTrade")
      return
    }
    stockStats.addStockTrade(trade)
  }

  private def reportStats(): Unit = {
    System.out.println("****** Shard " + kinesisShardId + " stats for last 1 minute ******\n" +
      stockStats.toString + "\n" +
      "****************************************************************\n")
  }

  private def resetStats(): Unit = {
    stockStats = new StockStats
  }

  override def shutdown(checkpointer: IRecordProcessorCheckpointer, reason: ShutdownReason): Unit = {
    StockTradeRecordProcessor.LOG.info(s"Checkpointing shard $kinesisShardId")
    if(reason == ShutdownReason.TERMINATE){
      checkpoint(checkpointer)
    }
  }

  private def checkpoint(checkpointer: IRecordProcessorCheckpointer): Unit = {
    StockTradeRecordProcessor.LOG.info(s"Checkpointing shard $kinesisShardId")
    try {
      checkpointer.checkpoint()
    } catch {
      case e: ShutdownException => StockTradeRecordProcessor.LOG.info("Caught shutdown exception, skipping checkpoint.", e)
      case e: ThrottlingException => StockTradeRecordProcessor.LOG.error("Caught throttling exception, skipping checkpoint.", e)
      case e: InvalidStateException => StockTradeRecordProcessor.LOG.error("Cannot save checkpoint to the DynamoDB table used by the Amazon Kinesis Client Library.", e)
    }
  }

}


object StockTradeRecordProcessor {
  val LOG = LogFactory.getLog(classOf[StockTradeRecordProcessor])
  val REPORTING_INTERVAL_MILLIS = 60000L
  val CHECKPOINT_INTERVAL_MILLIS = 60000L
}
