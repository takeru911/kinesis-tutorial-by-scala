#!/usr/bin/env python3

from aws_cdk import core

from cdk_data_stream.cdk_data_stream_stack import CdkDataStreamStack


app = core.App()
CdkDataStreamStack(app, "cdk-data-stream")

app.synth()
