package com.jaspervanmerle.qcij.api.model.response

import com.jaspervanmerle.qcij.api.model.QuantConnectBacktest

data class GetAllBacktestsResponse(val backtests: List<QuantConnectBacktest>)
