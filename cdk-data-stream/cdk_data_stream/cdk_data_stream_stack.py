from aws_cdk import (
    core,
    aws_kinesis as kinesis,
    aws_iam as iam
    )


class CdkDataStreamStack(core.Stack):

    def __init__(self, scope: core.Construct, id: str, **kwargs) -> None:
        super().__init__(scope, id, **kwargs)
        stream = self.create_kinesis_stream("StockTradeStream")
        self.create_iam(stream.stream_arn, "StockTradeStreamUser", "StockTradeStreamPolicy")

        
    def create_kinesis_stream(self, stream_name):
        kinesis_stream = kinesis.Stream(
            self,
            "stock_stream",
            stream_name=stream_name
            )

        return kinesis_stream

    def create_iam(self, stream_arn ,user_name, policy_name):
        user = iam.User(
            self,
            "iam_user_stock_stream",
            user_name=user_name            
            )
        
        policy = iam.Policy(
            self,
            "iam_policy_stock_stream",
            policy_name=policy_name,
            statements=[
                iam.PolicyStatement(
                    resources=[
                        stream_arn
                        ],
                    actions=[
                        "kinesis:DescribeStream",
                        "kinesis:PutRecord",
                        "kinesis:PutRecords",
                        "kinesis:GetShardIterator",
                        "kinesis:GetRecords",
                        "kinesis:ListShards",
                        "kinesis:DescribeStreamSummary",
                        "kinesis:RegisterStreamConsumer"
                        ],
                    ),
                iam.PolicyStatement(
                    resources=[
                        stream_arn + "/*"
                        ],
                    actions=[
                        "kinesis:SubscribeToShard",
                        "kinesis:DescribeStreamConsumer"
                        ],
                    ),
                iam.PolicyStatement(
                    resources=[
                        # dynamodbのテーブルができたら修正するとりあえず
                        "*"
                        ],
                    actions=[
                        "dynamodb:*"
                        ],
                    ),
                iam.PolicyStatement(
                    resources=[
                        "*"
                        ],
                    actions=[
                        "cloudwatch:PutMetricData"
                        ],
                    )
                ],
                users = [
                    user
                    ]
            )
