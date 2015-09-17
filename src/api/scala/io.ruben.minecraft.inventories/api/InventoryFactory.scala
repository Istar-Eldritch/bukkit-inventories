package io.ruben.minecraft.inventories.api

import io.ruben.minecraft.inventories.Inventory


/**
 * Created by istar on 17/09/15.
 */
trait InventoryFactory {
  def fromBukkit(inv: org.bukkit.inventory.Inventory, id: Option[Int]): InventoryX
}