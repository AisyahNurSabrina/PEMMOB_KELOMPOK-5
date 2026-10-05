package com.pemmob.foundu.ui.nav

import kotlinx.serialization.Serializable

// Type-safe Navigation: tiap tujuan navigasi adalah class/object, bukan string
// mentah ("item_list") yang gampang typo dan nggak ke-cek compiler.

@Serializable
object ItemListRoute

@Serializable
object CategoryListRoute

@Serializable
object PickupPointListRoute

@Serializable
data class ItemDetailRoute(val itemId: Long)

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object AdminLoginRoute

@Serializable
object ProfileRoute
