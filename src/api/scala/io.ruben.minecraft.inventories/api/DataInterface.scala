package io.ruben.minecraft.inventories.api

import java.util.UUID

import org.bukkit.inventory.ItemStack

import scala.concurrent.Future

/**
 * Created by istar on 17/09/15.
 */
trait DataInterface {
  def get(id: UUID): Future[ExtraInventory]
  def create: ExtraInventory
}