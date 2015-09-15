package io.ruben.minecraft.inventories

import com.comphenix.protocol.utility.StreamSerializer
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.{InventoryHolder, ItemStack, PlayerInventory}


/**
 * Created by istar on 15/09/15.
 */
case class Inventory(contents: String, armor: Option[String] = None, id: Option[Int] = None) {
  def toBukkit(owner: InventoryHolder): org.bukkit.inventory.Inventory = {
    val itemContents = Inventory.deserializeStacks(contents)
    armor match {
      case Some(arm) =>
        val inventory = Bukkit.createInventory(owner, InventoryType.PLAYER).asInstanceOf[PlayerInventory]
        inventory.setContents(itemContents)
        inventory.setArmorContents(Inventory.deserializeStacks(arm))
        inventory
      case None =>
        val inventory = Bukkit.createInventory(owner, nextNine(itemContents.length))
        inventory.setContents(itemContents)
        inventory
    }
  }
  
  private[this] def nextNine(n: Int): Int = if (n % 9 == 0) n else nextNine(n + 1)
}

object Inventory {

  val serializer = new StreamSerializer

  def fromBukkit(inventory: org.bukkit.inventory.Inventory, id: Option[Int] = None): Inventory = {
    inventory match {
      case inv: PlayerInventory =>
        Inventory(serializeStacks(inv.getContents), Option(serializeStacks(inv.getArmorContents)), id)
      case inv: _ =>
        Inventory(serializeStacks(inv.getContents), None, id)
    }
  }

  private def serializeStacks(items: Array[ItemStack]): String =
    items.map(item => serializer.serializeItemStack(item)).mkString(";")

  private def deserializeStacks(encoded: String): Array[ItemStack] =
    encoded.split(";").map(str => serializer.deserializeItemStack(str))

}