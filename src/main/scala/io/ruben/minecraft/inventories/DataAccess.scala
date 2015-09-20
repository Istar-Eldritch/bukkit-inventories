package io.ruben.minecraft.inventories

import java.util.UUID

import io.ruben.minecraft.ScalaLang
import io.ruben.minecraft.inventories.api.{ExtraInventory, DataInterface}
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import slick.driver.JdbcDriver

import scala.concurrent.Future

/**
 * Created by istar on 13/09/15.
 */
object DataAccess extends  DataInterface {
  val plugin:Main = JavaPlugin.getPlugin(classOf[Main])
  val scalaLang = Bukkit.getPluginManager.getPlugin("ScalaLang").asInstanceOf[ScalaLang]
  val driver: JdbcDriver = scalaLang.getDriver
  import driver.api._
  val db = scalaLang.getDb
  val inventories = TableQuery[Inventories]

  override def get(id: UUID): Future[ExtraInventory] = db.run(inventories.filter(_.id === id).result.head)

  override def create: ExtraInventory = InventoryImp()
}
