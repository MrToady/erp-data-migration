package com.organization.data.tables

import java.sql.Date

case class OrderHeader(orderNumber: Long, orderDate: Date, supplier: String, status: String)
