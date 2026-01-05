package com.fotsapp.pushify.plugin

import com.intellij.openapi.util.IconLoader
import com.intellij.ui.IconManager
import javax.swing.Icon

object Icons {
    // 24x24 for Tool Window icon as requested
    @JvmField
    val ToolWindow = load("/pushify.svg", 24, 24)

    // Original size (or large) for the header logo
    @JvmField
    val Header = IconLoader.getIcon("/pushify.svg", Icons::class.java)

    private fun load(path: String, width: Int, height: Int): Icon {
        return IconManager.getInstance().getIcon(path, Icons::class.java.classLoader).let {
            // If it's an SVG (ScalableIcon), we can resize it nicely
            if (it is com.intellij.openapi.util.ScalableIcon) {
                // The base icon might be large, so we prefer to rely on the IDE's SVG loader resizing capabilities
                // But ScalableIcon.scale() uses a float factor, which is relative to original size.
                // A safer bet for direct sizing with the modern API:
                IconLoader.getIcon(path, Icons::class.java.classLoader).let { original ->
                    if (original.iconWidth > 0) {
                        val scale = width.toFloat() / original.iconWidth.toFloat()
                        (original as com.intellij.openapi.util.ScalableIcon).scale(scale)
                    } else original
                }
            } else {
                it
            }
        }
    }
}

