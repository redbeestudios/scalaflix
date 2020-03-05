package models

/**
  * Package with configuration classes
  */
package object config {

  // $COVERAGE-OFF$ # a final val can't be taken by coverage tools
  final val DATABASE_DISPATCHER = "metrics.dispatchers.database" // final because is used in annotations
  // $COVERAGE-ON$

}
