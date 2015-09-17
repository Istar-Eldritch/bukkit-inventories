package io.ruben.minecraft.inventories.api

/**
 * Created by istar on 17/09/15.
 */
trait Callback[T] {
  def onSuccess(v: T): Unit
  def onFailure(err: Throwable)
}
