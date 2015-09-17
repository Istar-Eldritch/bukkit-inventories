package io.ruben.minecraft.inventories

import com.comphenix.protocol.utility.StreamSerializer
import io.ruben.minecraft.inventories.api.{Callback, InventoryX, InventoryFactory}
import org.bukkit.inventory.{ItemStack, PlayerInventory}
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import DataAccess._

import scala.util.{Failure, Success}

/**
 * Created by istar on 15/09/15.
 */
case class InventoryImp(contents: String, name: Option[String] = None, armor: Option[String] = None, id: Option[Int] = None) extends InventoryX {
  def getContents: Array[ItemStack] = Inventory.deserializeStacks(contents)
  def getArmor: Array[ItemStack] = armor match {
    case Some(arm) => Inventory.deserializeStacks(arm)
    case None => Array()
  }

  override def save: Future[Int] =
    db.run((inventories returning inventories.map(_.id)) += this)

  override def save(callback: Callback[Int]): Unit =
    save.onComplete {
      case Success(id) => callback.onSuccess(id)
      case Failure(err) => callback.onFailure(err)
    }
}

object Inventory extends InventoryFactory {

  val serializer = new StreamSerializer

  def fromBukkit(inventory: org.bukkit.inventory.Inventory, id: Option[Int] = None): InventoryImp = {
    inventory match {
      case inv: PlayerInventory =>
        InventoryImp(serializeStacks(inv.getContents), Option(serializeStacks(inv.getArmorContents)), None, id)
      case _ =>
        InventoryImp(serializeStacks(inventory.getContents), None, None, id)
    }
  }

  def serializeStacks(items: Array[ItemStack]): String =
    items.map(item => serializer.serializeItemStack(item)).mkString(";")

  def deserializeStacks(encoded: String): Array[ItemStack] =
    encoded.split(";").map(str => serializer.deserializeItemStack(str))

}

class Inventories(tag: Tag)
  extends Table[InventoryImp](tag, "USER") {

  def id =  column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def contents = column[String]("contents")
  def armor = column[String]("armor")
  def * = (contents, name.?, armor.?, id.?) <> ((InventoryImp.apply _).tupled, InventoryImp.unapply)
}