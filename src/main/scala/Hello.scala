import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {
  def id = column[Int]("SUP_ID", O.PrimaryKey) // This is the primary key column
  def name = column[String]("SUP_NAME")
  def street = column[String]("STREET")
  def city = column[String]("CITY")
  def state = column[String]("STATE")
  def zip = column[String]("ZIP")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, street, city, state, zip)
}

object Hello extends App {
  val db = Database.forConfig("mm")
  
  val suppliers = TableQuery[Suppliers]
  val f = try {
    val setup = DBIO.seq(
      suppliers.schema.create,
      suppliers ++= Seq((101, "Acme, Inc.",      "99 Market Street", "Groundsville", "CA", "95199"),
        ( 49, "Superior Coffee", "1 Party Place",    "Mendocino",    "CA", "95460"),
        (150, "The High Ground", "100 Coffee Lane",  "Meadows",      "CA", "93966")))
    db.run(setup)

  } finally db.close
  Await.result(f, Duration.Inf)
}
