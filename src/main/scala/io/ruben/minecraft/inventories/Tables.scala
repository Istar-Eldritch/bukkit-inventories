package io.ruben.minecraft.inventories

import slick.driver.H2Driver.api._


/**
 * Created by istar on 15/09/15.
 */
class Inventories(tag: Tag)
  extends Table[Inventory](tag, "USER") {

  def id =  column[Int]("id", O.PrimaryKey, O.AutoInc)
  def contents = column[String]("contents")
  def armor = column[String]("armor")
  def * = (contents, armor.?, id.?) <> ((Inventory.apply _).tupled, Inventory.unapply)
}
