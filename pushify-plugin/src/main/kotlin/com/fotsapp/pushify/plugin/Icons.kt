package com.fotsapp.pushify.plugin

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object Icons {
    // 13x13 is the standard size for Tool Window icons in IntelliJ Platform
    @JvmField
    val ToolWindow = load("/pushify.svg", 13)

    // Original size (or large) for the header logo
    @JvmField
    val Header = IconLoader.getIcon("/pushify.svg", Icons::class.java)

    private fun load(path: String, width: Int): Icon {
        val icon = IconLoader.getIcon(path, Icons::class.java)
        if (icon is com.intellij.openapi.util.ScalableIcon) {
            // Calculate scale based on the requested width vs actual width
            if (icon.iconWidth > 0) {
                 val scale = width.toFloat() / icon.iconWidth.toFloat()
                 return icon.scale(scale)
            }
        }
        return icon
    }
}
