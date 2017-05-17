/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.reports

import com.wegtam.amws.common.Request.ParameterValue

import scala.collection.immutable.Seq

/**
  * An enumeration of the types of reports that can be requested from
  * Amazon MWS. The report types are grouped logically into objects and
  * named to ease finding them in the AMWS documentation.
  *
  * @see http://docs.developer.amazonservices.com/en_US/reports/Reports_ReportType.html
  */
sealed trait ReportType {

  /**
    * The parameter value of the report type that must be used in the
    * query string.
    *
    * @return A string representation of the report type, usable in a query.
    */
  def toParameterValue: ParameterValue

}

object ReportType {
  // A list of all available report types. New report types must be added here!
  final val ALL
    : Seq[ReportType] = ListingReports.ALL ++ OrderReports.ALL ++ OrderTracking.ALL ++ PendingOrders.ALL ++ Performance.ALL ++ Settlement.ALL ++ SalesTax.ALL ++ BrowseTree.ALL

  /**
    * Return the report type described by the given parameter value.
    *
    * @param v A string representation of the report type.
    * @return An option to the appropriate report type.
    */
  def fromParameterValue(v: ParameterValue): Option[ReportType] = ALL.find(_.toParameterValue == v)

}

/**
  * Listing Reports
  */
object ListingReports {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    Inventory,
    AllListings,
    ActiveListings,
    InactiveListings,
    OpenListings,
    OpenListingsLite,
    OpenListingsLiter,
    CanceledListings,
    SoldListings,
    ListingQualityAndSuppressedListings
  )

  /**
    * Tab-delimited flat file open listings report that contains the SKU,
    * ASIN, Price, and Quantity fields. For Marketplace and Seller Central
    * sellers.
    */
  case object Inventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FLAT_FILE_OPEN_LISTINGS_DATA_"
  }

  /**
    * Tab-delimited flat file detailed all listings report. For Marketplace
    * and Seller Central sellers.
    */
  case object AllListings extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_LISTINGS_ALL_DATA_"
  }

  /**
    * Tab-delimited flat file detailed active listings report. For
    * Marketplace and Seller Central sellers.
    */
  case object ActiveListings extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_LISTINGS_DATA_"
  }

  /**
    * Tab-delimited flat file detailed inactive listings report. For
    * Marketplace and Seller Central sellers.
    */
  case object InactiveListings extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_LISTINGS_INACTIVE_DATA_"
  }

  /**
    * Tab-delimited flat file open listings report.
    */
  case object OpenListings extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_LISTINGS_DATA_BACK_COMPAT_"
  }

  /**
    * Tab-delimited flat file active listings report that contains only the
    * SKU, ASIN, Price, and Quantity fields for items that have a quantity
    * greater than zero. For Marketplace and Seller Central sellers.
    */
  case object OpenListingsLite extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_LISTINGS_DATA_LITE_"
  }

  /**
    * Tab-delimited flat file active listings report that contains only the
    * SKU and Quantity fields for items that have a quantity greater than
    * zero. For Marketplace and Seller Central sellers.
    */
  case object OpenListingsLiter extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_LISTINGS_DATA_LITER_"
  }

  /**
    * Tab-delimited flat file canceled listings report. For Marketplace and
    * Seller Central sellers.
    */
  case object CanceledListings extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_CANCELLED_LISTINGS_DATA_"
  }

  /**
    * Tab-delimited flat file sold listings report that contains items sold
    * on Amazon's retail website. For Marketplace and Seller Central sellers.
    */
  case object SoldListings extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_CONVERGED_FLAT_FILE_SOLD_LISTINGS_DATA_"
  }

  /**
    * Tab-delimited flat file listing quality and suppressed listing report
    * that contains your listing information that is incomplete or
    * incorrect. For Marketplace and Seller Central sellers.
    */
  case object ListingQualityAndSuppressedListings extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_MERCHANT_LISTINGS_DEFECT_DATA_"
  }

}

/**
  * Order Reports
  */
object OrderReports {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    Unshipped,
    ScheduledXml,
    RequestedOrScheduledFlatFile,
    FlatFile
  )

  /**
    * Tab-delimited flat file report that contains only orders that are not
    * confirmed as shipped. Can be requested or scheduled. For Marketplace
    * and Seller Central sellers.
    */
  case object Unshipped extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FLAT_FILE_ACTIONABLE_ORDER_DATA_"
  }

  /**
    * Scheduled XML order report. For Seller Central sellers only.
    */
  case object ScheduledXml extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_ORDERS_DATA_"
  }

  /**
    * Tab-delimited flat file order report that can be requested or
    * scheduled. The report shows orders from the previous 60 days.
    * For Marketplace and Seller Central sellers.
    * <br/>
    * Seller Central sellers can only schedule one `_GET_ORDERS_DATA_` or
    * `_GET_FLAT_FILE_ORDERS_DATA_` report at a time. If you have one of
    * these reports scheduled and you schedule a new report, the existing
    * report will be canceled.
    * <br/>
    * Marketplace sellers can only schedule one `_GET_FLAT_FILE_ORDERS_DATA_`
    * or `_GET_CONVERGED_FLAT_FILE_ORDER_REPORT_DATA_` report at a time.
    * If you have one of these reports scheduled and you schedule a new
    * report, the existing report will be canceled.
    * <br/>
    * '''Note: The format of this report will differ slightly depending on
    * whether it is scheduled or requested.'''
    */
  case object RequestedOrScheduledFlatFile extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FLAT_FILE_ORDERS_DATA_"
  }

  /**
    * Tab-delimited flat file order report that can be requested or
    * scheduled. For Marketplace sellers only.
    * <br/>
    * You can only schedule one `_GET_FLAT_FILE_ORDERS_DATA_` or
    * `_GET_CONVERGED_FLAT_FILE_ORDER_REPORT_DATA_` report at a time.
    * If you have one of these reports scheduled and you schedule a new
    * report, the existing report will be canceled.
    * <br/>
    * '''Note: The format of this report will differ slightly depending on
    * whether it is scheduled or requested. For example, the format for the
    * dates will differ, and the ship-method column is only returned when
    * the report is requested.'''
    */
  case object FlatFile extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_CONVERGED_FLAT_FILE_ORDER_REPORT_DATA_"
  }

}

/**
  * Order Tracking Reports
  */
object OrderTracking {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    FlatFileByLastUpdate,
    FlatFileByOrderDate,
    XmlByLastUpdate,
    XmlByOrderDate
  )

  /**
    * Tab-delimited flat file report that shows all orders updated in the
    * specified period. Cannot be scheduled. For all sellers.
    */
  case object FlatFileByLastUpdate extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_"
  }

  /**
    * Tab-delimited flat file report that shows all orders that were
    * placed in the specified period. Cannot be scheduled. For all sellers.
    */
  case object FlatFileByOrderDate extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_"
  }

  /**
    * XML report that shows all orders updated in the specified period.
    * Cannot be scheduled. For all sellers.
    */
  case object XmlByLastUpdate extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_XML_ALL_ORDERS_DATA_BY_LAST_UPDATE_"
  }

  /**
    * XML report that shows all orders that were placed in the specified
    * period. Cannot be scheduled. For all sellers.
    */
  case object XmlByOrderDate extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_XML_ALL_ORDERS_DATA_BY_ORDER_DATE_"
  }

}

/**
  * Pending Order Reports
  */
object PendingOrders {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    FlatFile,
    Xml,
    ConvergedFlatFile
  )

  /**
    * Tab-delimited flat file report that can be requested or scheduled
    * that shows all pending orders. For all sellers.
    */
  case object FlatFile extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FLAT_FILE_PENDING_ORDERS_DATA_"
  }

  /**
    * XML report that can be requested or scheduled that shows all pending
    * orders. Can only be scheduled using Amazon MWS.
    */
  case object Xml extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_PENDING_ORDERS_DATA_"
  }

  /**
    * Flat file report that can be requested or scheduled that shows all
    * pending orders. For Marketplace sellers.
    */
  case object ConvergedFlatFile extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_CONVERGED_FLAT_FILE_PENDING_ORDERS_DATA_"
  }

}

/**
  * Performance Reports
  */
object Performance {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    FlatFile,
    XmlCustomerMetrics
  )

  /**
    * Tab-delimited flat file that returns negative and neutral feedback
    * (one to three stars) from buyers who rated your seller performance.
    * For all sellers.
    */
  case object FlatFile extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_SELLER_FEEDBACK_DATA_"
  }

  /**
    * XML file that contains the individual performance metrics data from
    * the Seller Central dashboard. For all sellers.
    */
  case object XmlCustomerMetrics extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_V1_SELLER_PERFORMANCE_REPORT_"
  }

}

/**
  * Settlement Reports
  */
object Settlement {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    FlatFile,
    Xml,
    FlatFileV2
  )

  /**
    * Tab-delimited flat file settlement report that is automatically
    * scheduled by Amazon; it cannot be requested through `RequestReport`.
    * For all sellers.
    */
  case object FlatFile extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_"
  }

  /**
    * XML file settlement report that is automatically scheduled by Amazon;
    * it cannot be requested through `RequestReport`. For Seller Central
    * sellers only.
    */
  case object Xml extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_V2_SETTLEMENT_REPORT_DATA_XML_"
  }

  /**
    * Tab-delimited flat file alternate version of the Flat File Settlement
    * Report that is automatically scheduled by Amazon; it cannot be
    * requested through `RequestReport`. Price columns are condensed into
    * three general purpose columns: amounttype, amountdescription, and
    * amount. For Seller Central sellers only.
    */
  case object FlatFileV2 extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2_"
  }

}

/**
  * Fulfillment By Amazon (FBA) Sales Reports
  */
object FBASales {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    FulfilledShipments,
    FlatFileAllOrdersByLastUpdate,
    FlatFileAllOrdersByOrderDate,
    XmlAllOrdersByLastUpdate,
    XmlAllOrdersByOrderDate,
    CustomerShipmentSales,
    Promotions,
    CustomerTaxes
  )

  /**
    * Tab-delimited flat file. Contains detailed order/shipment/item
    * information including price, address, and tracking data. You can
    * request up to one month of data in a single report. Content updated
    * near real-time in Europe (EU), Japan, and North America (NA). In
    * China, content updated daily. For FBA sellers only. For Marketplace
    * and Seller Central sellers.
    *
    * <p><strong>Note:</strong> In Japan, EU, and NA, in most cases, there
    * will be a delay of approximately one to three hours from the time a
    * fulfillment order ships and the time the items in the fulfillment
    * order appear in the report. In some rare cases there could be a delay
    * of up to 24 hours.</p>
    */
  case object FulfilledShipments extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_AMAZON_FULFILLED_SHIPMENTS_DATA_"
  }

  /**
    * Tab-delimited flat file. Returns all orders updated in the specified
    * date range regardless of fulfillment channel or shipment status. This
    * report is intended for order tracking, not to drive your fulfillment
    * process; it does not include customer identifying information and
    * scheduling is not supported. For all sellers.
    */
  case object FlatFileAllOrdersByLastUpdate extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_"
  }

  /**
    * Tab-delimited flat file. Returns all orders placed in the specified
    * date range regardless of fulfillment channel or shipment status. This
    * report is intended for order tracking, not to drive your fulfillment
    * process; it does not include customer identifying information and
    * scheduling is not supported. For all sellers.
    */
  case object FlatFileAllOrdersByOrderDate extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_"
  }

  /**
    * XML file order report that returns all orders updated in the
    * specified date range regardless of fulfillment channel or shipment
    * status. This report is intended for order tracking, not to drive your
    * fulfillment process; it does not include customer identifying
    * information and scheduling is not supported. For all sellers.
    */
  case object XmlAllOrdersByLastUpdate extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_XML_ALL_ORDERS_DATA_BY_LAST_UPDATE_"
  }

  /**
    * XML file order report that returns all orders placed in the specified
    * date range regardless of fulfillment channel or shipment status. This
    * report is intended for order tracking, not to drive your fulfillment
    * process; it does not include customer identifying information and
    * scheduling is not supported. For all sellers.
    */
  case object XmlAllOrdersByOrderDate extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_XML_ALL_ORDERS_DATA_BY_ORDER_DATE_"
  }

  /**
    * Tab-delimited flat file. Contains condensed item level data on
    * shipped FBA customer orders including price, quantity, and ship to
    * location. Content updated near real-time in Europe (EU), Japan, and
    * North America (NA). In China, content updated daily. For FBA sellers
    * only. For Marketplace and Seller Central sellers.
    *
    * <p><strong>Note:</strong> In Japan, EU, and NA, in most cases, there
    * will be a delay of approximately one to three hours from the time a
    * fulfillment order ships and the time the items in the fulfillment
    * order appear in the report. In some rare cases there could be a delay
    * of up to 24 hours.</p>
    */
  case object CustomerShipmentSales extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_SALES_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains promotions applied to FBA customer
    * orders sold through Amazon; e.g. Super Saver Shipping. Content
    * updated daily. This report is only available to FBA sellers in the
    * North America (NA) region. For Marketplace and Seller Central sellers.
    */
  case object Promotions extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_PROMOTION_DATA_"
  }

  /**
    * Tab-delimited flat file for tax-enabled US sellers. This report
    * contains data through February 28, 2013. All new transaction data can
    * be found in the [[com.wegtam.amws.reports.SalesTax.SalesTax]] Report.
    * For FBA sellers only. For Marketplace and Seller Central sellers.
    */
  case object CustomerTaxes extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_FULFILLMENT_CUSTOMER_TAXES_DATA_"
  }
}

/**
  * Fulfillment By Amazon (FBA) Inventory Reports
  */
object FBAInventory {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    AmazonFulfilledInventory,
    MultiCountryInventory,
    DailyInventoryHistory,
    MonthlyInventoryHistory,
    ReceivedInventory,
    ReservedInventory,
    InventoryEventDetails,
    InventoryAdjustments,
    InventoryHealth,
    ManageInventory,
    ManageInventoryArchived,
    CrossBorderInventoryMovement,
    RestockInventory,
    InboundPerformance,
    StrandedInventory,
    BulkFixStrandedInventory,
    InventoryAge,
    ManageExcessInventory
  )

  /**
    * Tab-delimited flat file. Content updated in near real-time. For FBA
    * sellers only. For Marketplace and Seller Central sellers.
    */
  case object AmazonFulfilledInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_AFN_INVENTORY_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains quantity available for local
    * fulfillment by country, helping Multi-Country Inventory sellers in
    * Europe track their FBA inventory. Content updated in near-real time.
    * This report is only available to FBA sellers in European (EU)
    * marketplaces. For Seller Central sellers.
    */
  case object MultiCountryInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_AFN_INVENTORY_DATA_BY_COUNTRY_"
  }

  /**
    * Tab-delimited flat file. Contains historical daily snapshots of your
    * available inventory in Amazon’s fulfillment centers including
    * quantity, location and disposition. Content updated daily. For FBA
    * sellers only. For Marketplace and Seller Central sellers.
    */
  case object DailyInventoryHistory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_FULFILLMENT_CURRENT_INVENTORY_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains historical monthly snapshots of
    * your available inventory in Amazon’s fulfillment centers including
    * average and end-of-month quantity, location and disposition. Content
    * updated daily. For FBA sellers only. For Marketplace and Seller
    * Central sellers.
    */
  case object MonthlyInventoryHistory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_FULFILLMENT_MONTHLY_INVENTORY_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains inventory that has completed the
    * receive process at Amazon’s fulfillment centers. Content updated
    * daily. For FBA sellers only. For Marketplace and Seller Central
    * sellers.
    */
  case object ReceivedInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_FULFILLMENT_INVENTORY_RECEIPTS_DATA_"
  }

  /**
    * Tab-delimited flat file. Provides data about the number of reserved
    * units in your inventory. Content updated in near real-time. For FBA
    * sellers only. For Marketplace and Seller Central sellers.
    */
  case object ReservedInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_RESERVED_INVENTORY_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains history of inventory events (e.g.
    * receipts, shipments, adjustments etc.) by SKU and Fulfillment Center.
    * Content updated daily. For FBA sellers only. For Marketplace and
    * Seller Central sellers.
    */
  case object InventoryEventDetails extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_FULFILLMENT_INVENTORY_SUMMARY_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains corrections and updates to your
    * inventory in response to issues such as damage, loss, receiving
    * discrepancies, etc. Content updated daily. For FBA sellers only.
    * For Marketplace and Seller Central sellers.
    */
  case object InventoryAdjustments extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_INVENTORY_ADJUSTMENTS_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains information about inventory age,
    * condition, sales volume, weeks of cover, and price. Content updated
    * daily. For FBA Sellers only. For Marketplace and Seller Central
    * sellers.
    */
  case object InventoryHealth extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains current details of active (not
    * archived) inventory including condition, quantity and volume. Content
    * updated in near real-time. For FBA sellers only. For Marketplace and
    * Seller Central sellers.
    */
  case object ManageInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains current details of all (including
    * archived) inventory including condition, quantity and volume. Content
    * updated in near real-time. For FBA sellers only. For Marketplace and
    * Seller Central sellers.
    */
  case object ManageInventoryArchived extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_MYI_ALL_INVENTORY_DATA_"
  }

  /**
    * Tab delimited flat file. Contains historical data of shipments that
    * crossed country borders. These could be export shipments or shipments
    * using Amazon's European Fulfillment Network (note that Amazon's
    * European Fulfillment Network is for Seller Central sellers only).
    * Content updated daily. For Marketplace and Seller Central sellers.
    */
  case object CrossBorderInventoryMovement extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_CROSS_BORDER_INVENTORY_MOVEMENT_DATA_"
  }

  /**
    * Tab delimited flat file. Provides recommendations on products to
    * restock, suggested order quantities, and reorder dates. For more
    * information, see Restock Inventory Report. Content updated in near
    * real-time. This report is only available to FBA sellers in the US
    * marketplace.
    */
  case object RestockInventory extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_RESTOCK_INVENTORY_RECOMMENDATIONS_REPORT_"
  }

  /**
    * Tab-delimited flat file. Contains inbound shipment problems by
    * product and shipment. Content updated daily. For Marketplace and
    * Seller Central sellers. This report is only available to FBA sellers.
    */
  case object InboundPerformance extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_INBOUND_NONCOMPLIANCE_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains a breakdown of inventory in
    * stranded status, including recommended actions. Content updated in
    * near real-time. This report is only available to FBA sellers in the
    * US, India, and Japan marketplaces. For more information, see
    * Stranded Inventory Report.
    */
  case object StrandedInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_STRANDED_INVENTORY_UI_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains a list of stranded inventory.
    * Update the listing information in this report file and then submit
    * the file using the Flat File Inventory Loader Feed
    * (`_POST_FLAT_FILE_INVLOADER_DATA_`) of the Feeds API section. Content
    * updated in near real-time. This report is only available to FBA
    * sellers in the US, India, and Japan marketplaces. For more
    * information, see Bulk Fix Stranded Inventory Report.
    */
  case object BulkFixStrandedInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_STRANDED_INVENTORY_LOADER_DATA_"
  }

  /**
    * Tab-delimited flat file. Indicates the age of inventory, which helps
    * sellers take action to avoid paying the Long Term Storage Fee.
    * Content updated daily. This report is only available to FBA sellers
    * in the US, India, and Japan marketplaces. For more information, see
    * Inventory Age Report.
    */
  case object InventoryAge extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_INVENTORY_AGED_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains listings with excess inventory,
    * which helps sellers take action to sell through faster. Content
    * updated in near real-time. This report is only available to FBA
    * sellers in the US, India, and Japan marketplaces. For more
    * information, see Excess Inventory Report.
    */
  case object ManageExcessInventory extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_EXCESS_INVENTORY_DATA_"
  }
}

/**
  * Fulfillment By Amazon (FBA) Payments Reports
  */
object FBAPayments {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    FreePreview,
    Reimbursements
  )

  /**
    * Tab-delimited flat file. Contains the estimated Amazon Selling and
    * Fulfillment Fees for your FBA inventory with active offers. The
    * content is updated at least once every 72 hours. To successfully
    * generate a report, specify the '''StartDate''' parameter of a minimum
    * 72 hours prior to NOW and '''EndDate''' to NOW. For FBA sellers in
    * the NA and EU only. For Marketplace and Seller Central sellers.
    */
  case object FreePreview extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains itemized details of your inventory
    * reimbursements including the reason for the reimbursement. Content
    * updated daily. For FBA sellers only. For Marketplace and Seller
    * Central sellers.
    */
  case object Reimbursements extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_REIMBURSEMENTS_DATA_"
  }
}

/**
  * Fulfillment By Amazon (FBA) Customer Concessions Reports
  */
object FBACustomerConcessions {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    Returns,
    Replacements
  )

  /**
    * Tab-delimited flat file. Contains customer returned items received at
    * an Amazon fulfillment center, including Return Reason and Disposition.
    * Content updated daily. For FBA sellers only. For Marketplace and
    * Seller Central sellers.
    */
  case object Returns extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA_"
  }

  /**
    * Tab-delimited flat file. Contains replacements that have been issued
    * to customers for completed orders. Content updated daily. For FBA
    * sellers only. For Marketplace and Seller Central sellers. Available
    * in the US and China (CN) only.
    */
  case object Replacements extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_REPLACEMENT_DATA_"
  }
}

/**
  * Fulfillment By Amazon (FBA) Removals Reports
  */
object FBARemovals {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    RecommendedRemoval,
    RemovalOrderDetails,
    RemovalShipmentDetails
  )

  /**
    * Tab-delimited flat file. The report identifies sellable items that
    * will be 365 days or older during the next Long-Term Storage cleanup
    * event, if the report is generated within six weeks of the cleanup
    * event date. The report includes both sellable and unsellable items.
    * Content updated daily. For FBA sellers. For Marketplace and Seller
    * Central sellers.
    */
  case object RecommendedRemoval extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FBA_RECOMMENDED_REMOVAL_DATA_"
  }

  /**
    * Tab-delimited flat file. This report contains all the removal orders,
    * including the items in each removal order, placed during any given
    * time period. This can be used to help reconcile the total number of
    * items requested to be removed from an Amazon fulfillment center with
    * the actual number of items removed along with the status of each item
    * in the removal order. Content updated in near real-time. For FBA
    * sellers. For Marketplace and Seller Central sellers.
    */
  case object RemovalOrderDetails extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA_"
  }

  /**
    * Tab-delimited flat file. This report provides shipment tracking
    * information for all removal orders and includes the items that have
    * been removed from an Amazon fulfillment center for either a single
    * removal order or for a date range. This report will not include
    * canceled returns or disposed items; it is only for shipment
    * information. Content updated in near real-time. For FBA sellers.
    * For Marketplace and Seller Central sellers.
    */
  case object RemovalShipmentDetails extends ReportType {
    override def toParameterValue: ParameterValue =
      "_GET_FBA_FULFILLMENT_REMOVAL_SHIPMENT_DETAIL_DATA_"
  }
}

/**
  * Sales Tax Reports
  */
object SalesTax {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    SalesTax
  )

  /**
    * Tab-delimited flat file for tax-enabled US sellers. Content updated
    * daily. This report cannot be requested or scheduled. You must
    * generate the report from the Tax Document Library in Seller Central.
    * After the report has been generated, you can download the report
    * using the `GetReportList` and `GetReport` operations. For Marketplace
    * and Seller Central sellers.
    */
  case object SalesTax extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_FLAT_FILE_SALES_TAX_DATA_"
  }

}

/**
  * Browse Tree Reports
  */
object BrowseTree {
  // A list of all available report types. New report types must be added here!
  @SuppressWarnings(Array("org.wartremover.warts.Product", "org.wartremover.warts.Serializable"))
  final val ALL: Seq[ReportType] = Seq(
    BrowseTree
  )

  /**
    * XML report that provides browse tree hierarchy information and node
    * refinement information for the Amazon retail website in any
    * marketplace.
    * Can be requested or scheduled. For Marketplace and Seller Central
    * sellers.
    */
  case object BrowseTree extends ReportType {
    override def toParameterValue: ParameterValue = "_GET_XML_BROWSE_TREE_DATA_"
  }

}
