package io.ruben.minecraft.inventories

import slick.driver.H2Driver.api._


/**
 * Created by istar on 15/09/15.
 */
class Inventories(tag: Tag)
  extends Table[Inventory](tag, "USER") {

  def id =  column[Int]("id", O.PrimaryKey)
  def contents = column[String]("contents")
  def * = (contents, id.?) <> ((Inventory.apply _).tupled, Inventory.unapply)
}
