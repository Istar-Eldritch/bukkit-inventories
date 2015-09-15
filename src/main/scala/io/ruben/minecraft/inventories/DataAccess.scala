package io.ruben.minecraft.inventories

import org.bukkit.plugin.java.JavaPlugin
import slick.driver.H2Driver.api._


/**
 * Created by istar on 13/09/15.
 */
object DataAccess {
  val plugin:InventoriesPlugin = JavaPlugin.getPlugin(classOf[InventoriesPlugin])
  val db = Database.forURL(s"jdbc:h2:${plugin.getDataFolder.getAbsolutePath}/inventories", driver = "org.h2.Driver")

  val inventories = TableQuery[Inventories]
}