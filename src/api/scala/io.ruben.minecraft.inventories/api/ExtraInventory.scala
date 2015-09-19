package io.ruben.minecraft.inventories.api

import java.util.UUID

import org.bukkit.inventory.ItemStack

import scala.concurrent.Future

/**
 * Created by istar on 18/09/15.
 */
trait ExtraInventory {
  def contents: Array[ItemStack]
  def armor: Option[Array[ItemStack]]
  def name: Option[String]
  def id: UUID

  def setContents(items: Array[ItemStack]): ExtraInventory
  def setArmor(items: Option[Array[ItemStack]]): ExtraInventory
  def setName(name: Option[String]): ExtraInventory

  def save: Future[ExtraInventory]
}