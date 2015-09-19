package io.ruben.minecraft.inventories

import java.util.UUID

import io.ruben.minecraft.inventories.api.{ExtraInventory, DataInterface}
import org.bukkit.plugin.java.JavaPlugin
import slick.driver.H2Driver.api._

import scala.concurrent.Future


/**
 * Created by istar on 13/09/15.
 */
object DataAccess extends  DataInterface {
  val plugin:Main = JavaPlugin.getPlugin(classOf[Main])
  val db = Database.forURL(s"jdbc:h2:${plugin.getDataFolder.getAbsolutePath}/inventories", driver = "org.h2.Driver")
  val inventories = TableQuery[Inventories]

  override def get(id: UUID): Future[ExtraInventory] = db.run(inventories.filter(_.id === id).result.head)

  override def create: ExtraInventory = InventoryImp()
}
