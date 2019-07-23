package processor

import com.amazonaws.services.kinesis.clientlibrary.interfaces.{IRecordProcessor, IRecordProcessorFactory}

class StockTradeRecordProcessorFactory extends IRecordProcessorFactory{

  override def createProcessor(): IRecordProcessor = {
    println("create!!!")
    new StockTradeRecordProcessor()
  }
}
