package com.organization.data.tables

case class OrderPositionToSave (orderNumber: Long,
                           positionNumber: Long,
                           goodId: Long,
                           quantity: Int,
                           yearMonth: String)
