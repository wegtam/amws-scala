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
  * Amazon MWS.
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
    * @return The appropriate report type.
    */
  @throws[NoSuchElementException](
    cause = "The given representation is not included in the list of report types."
  )
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
