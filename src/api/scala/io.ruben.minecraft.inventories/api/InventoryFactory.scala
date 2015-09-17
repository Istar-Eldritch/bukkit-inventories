package io.ruben.minecraft.inventories.api

/**
 * Created by istar on 17/09/15.
 */
trait InventoryFactory {
  def fromBukkit(inv: org.bukkit.inventory.Inventory, id: Option[Int]): InventoryX
}