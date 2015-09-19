package io.ruben.minecraft.inventories

import java.util.UUID

import com.comphenix.protocol.utility.StreamSerializer
import io.ruben.minecraft.inventories.api.ExtraInventory
import org.bukkit.inventory.ItemStack
import slick.driver.H2Driver.api._
import DataAccess._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

case class InventoryImp(contents: Array[ItemStack] = Array.empty, armor: Option[Array[ItemStack]] = None, name: Option[String] = None, id:UUID = UUID.randomUUID()) extends ExtraInventory {
  override def save: Future[ExtraInventory] = db.run(inventories.insertOrUpdate(this)).map { _ => this }

  override def setContents(items: Array[ItemStack]): ExtraInventory = copy(contents = items)

  override def setArmor(items: Option[Array[ItemStack]]): ExtraInventory = copy(armor = items)

  override def setName(name: Option[String]): ExtraInventory = copy(name = name)
}

object InventoryImp extends {

  val serializer = new StreamSerializer

  def serializeStacks(items: Array[ItemStack]): String =
    items.map(item => serializer.serializeItemStack(item)).mkString(";")

  def deserializeStacks(encoded: String): Array[ItemStack] =
    if (encoded.length > 0)
      encoded.split(";").map(str => serializer.deserializeItemStack(str))
    else
      Array.empty

  def fromRow(contents: String, armor: Option[String], name: Option[String], id: UUID): InventoryImp = {
    val dContents = deserializeStacks(contents)
    val dArmor = armor.collect {case arm => deserializeStacks(arm)}
    InventoryImp(dContents, dArmor, name, id)
  }

  def toRow(inv: ExtraInventory) = {
    val sContents = serializeStacks(inv.contents)
    val sArmor = inv.armor.collect{ case arm => serializeStacks(arm) }
    Some((sContents, sArmor, inv.name, inv.id))
  }

}

class Inventories(tag: Tag)
  extends Table[InventoryImp](tag, "INVENTORY") {

  def id =  column[UUID]("id", O.PrimaryKey, O.SqlType("UUID"))
  def contents = column[String]("contents")
  def name = column[Option[String]]("name")
  def armor = column[Option[String]]("armor")

  def * = (contents, armor, name, id) <> ((InventoryImp.fromRow _).tupled, InventoryImp.toRow)
}