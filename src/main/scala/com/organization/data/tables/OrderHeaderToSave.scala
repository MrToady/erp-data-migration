package com.organization.data.tables

import java.sql.Date

case class OrderHeaderToSave(orderNumber: Long,
                        orderDate: Date,
                        supplier: String,
                        status: String,
                        yearMonth: String)
