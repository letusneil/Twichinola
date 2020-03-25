package com.letusneil.twichinola.util

const val LIMIT = 15

fun <T> List<T>.offset(): Int {
  return if (isNullOrEmpty()) 0 else LIMIT + size
}