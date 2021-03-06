package com.aouerfelli.subwatcher.util.extensions

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

inline val Context.layoutInflater: LayoutInflater
  get() = LayoutInflater.from(this)

@ColorInt
fun Context.getThemeColor(@AttrRes attrResId: Int, @ColorInt defaultValue: Int = Color.BLACK): Int {
  return obtainStyledAttributes(null, intArrayOf(attrResId)).use { it.getColor(0, defaultValue) }
}
