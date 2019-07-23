package utils

import com.amazonaws.AmazonClientException
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider

object CredentialUtils {

  def getCredentialsProvider(): AWSCredentialsProvider = {
    try {
      new ProfileCredentialsProvider("kinesis")
    } catch {
      case e: Exception => throw new AmazonClientException("Cannot load the credential")
    }
  }
}
