package io.ruben.minecraft.inventories.api

import org.bukkit.inventory.ItemStack

import scala.concurrent.Future

/**
 * Created by istar on 17/09/15.
 */
trait InventoryX {
  def getContents: Array[ItemStack]
  def getArmor: Array[ItemStack]
  def save: Future[Int]
  def save(callback: Callback[Int])
}