package com.organization.data

import java.util.Properties

import com.organization.data.config.{AppConfig, DBConfig}
import com.organization.data.tables.{OrderHeader, OrderHeaderToSave, OrderPosition, OrderPositionToSave}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object MigrationDriver extends App with LazyLogging {
  require(args.length == 4)

  private val orderHeaderPath = args(0)
  private val orderPositionPath = args(1)
  private val orderHeaderTableName = args(2)
  private val orderPositionTableName = args(3)

  private val config = AppConfig()

  val ss = SparkSession.builder
    .master("yarn")
    .appName("test app")
    .getOrCreate

  private val orderHeaderQuery = s"SELECT * FROM $orderHeaderTableName where date < $oneYearOldDate and date > $lastUploadDate"
  private val orderPositionQuery = s"SELECT * FROM $orderPositionTableName where date < $oneYearOldDate and date > $lastUploadDate"

  import ss.implicits._

  dfFromQuery(ss, config.dBConfig, orderHeaderTableName, orderHeaderQuery)
    .as[OrderHeader]
    .map(prepareOrderHeaderToSave)
    .write
    .partitionBy("year","month")
    .parquet(orderHeaderPath)

  dfFromQuery(ss, config.dBConfig, orderPositionTableName, orderPositionQuery)
    .as[OrderPosition]
    .map(prepareOrderPositionToSave)
    .write
    .partitionBy("year","month")
    .parquet(orderPositionPath)

  val prepareOrderPositionToSave: OrderPosition => OrderPositionToSave = ??? //функции маппинга, добавление года и месяца для партиционирования
  val prepareOrderHeaderToSave: OrderHeader => OrderHeaderToSave = ???

  def dfFromQuery(spark: SparkSession, config: DBConfig, table: String, query: String) = {
    val properties = new Properties()
    properties.setProperty("user", config.user)
    properties.setProperty("password", config.password)
    properties.setProperty("driver", config.driver)
    properties.setProperty("dbTable", query)

    spark.read.jdbc(config.url, table, properties)
  }

  def oneYearOldDate: String = ??? // вычисление даты для "старых" данных
  def lastUploadDate: String = ??? // вычисление даты (года и месяца) когда последний раз архивировались данные
}
