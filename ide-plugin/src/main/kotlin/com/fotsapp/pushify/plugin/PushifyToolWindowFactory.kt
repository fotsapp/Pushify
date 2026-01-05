package com.fotsapp.pushify.plugin

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.labels.LinkLabel
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class PushifyToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val pushifyToolWindow = PushifyToolWindow(toolWindow)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(pushifyToolWindow.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
    }

    class PushifyToolWindow(toolWindow: ToolWindow) {
        private val contentPanel = JPanel(GridBagLayout())
        private val projectIdField = JBTextField()
        private val bearerTokenField = JTextArea(3, 20).apply {
            lineWrap = true
            wrapStyleWord = true
            border = BorderFactory.createEtchedBorder()
        }
        private val deviceTokenField = JBTextField()
        private val titleField = JBTextField()
        private val bodyField = JBTextField()
        private val deepLinkField = JBTextField()
        private val sendButton = JButton("Send Notification")
        private val statusLabel = JTextArea(5, 20).apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
            background = null
            border = null
        }

        init {
            layoutComponents()
            setupListeners()
        }

        private fun layoutComponents() {
            val c = GridBagConstraints()
            c.fill = GridBagConstraints.HORIZONTAL
            c.insets = JBUI.insets(5)
            c.weightx = 1.0
            c.gridx = 0
            c.gridy = 0

            // Header with Icon
            val headerPanel = JPanel(GridBagLayout())
            val hc = GridBagConstraints()
            hc.anchor = GridBagConstraints.CENTER
            headerPanel.add(JBLabel(Icons.Header).apply { 
                iconTextGap = 10
            }, hc)
            contentPanel.add(headerPanel, c)

            c.gridy++
            // Configuration Section
            addLabel("Project ID:", c)
            c.gridy++
            contentPanel.add(projectIdField, c)

            c.gridy++
            addLabel("Bearer Token:", c)
            c.gridy++
            contentPanel.add(JScrollPane(bearerTokenField), c)

            c.gridy++
            val helpLink = LinkLabel<String>("How to get Bearer Token?", null)
            helpLink.setListener({ _, _ ->
                BrowserUtil.browse("https://firebase.google.com/docs/cloud-messaging/send/v1-api#provide-credentials-manually")
            }, null)
            helpLink.toolTipText = "See 'Provide credentials manually' section"
            
            // Add a small info text under the link
            val infoPanel = JPanel(GridBagLayout())
            val ic = GridBagConstraints()
            ic.anchor = GridBagConstraints.WEST
            ic.gridx = 0
            infoPanel.add(helpLink, ic)
            
            contentPanel.add(infoPanel, c)

            c.gridy++
            contentPanel.add(JSeparator(), c)

            // Message Section
            c.gridy++
            addLabel("Device Token:", c)
            c.gridy++
            contentPanel.add(deviceTokenField, c)

            c.gridy++
            addLabel("Title:", c)
            c.gridy++
            contentPanel.add(titleField, c)

            c.gridy++
            addLabel("Body:", c)
            c.gridy++
            contentPanel.add(bodyField, c)

            c.gridy++
            addLabel("Deeplink (optional):", c)
            c.gridy++
            contentPanel.add(deepLinkField, c)

            c.gridy++
            c.fill = GridBagConstraints.NONE
            c.anchor = GridBagConstraints.CENTER
            contentPanel.add(sendButton, c)

            c.gridy++
            c.fill = GridBagConstraints.BOTH
            c.weighty = 1.0
            contentPanel.add(JScrollPane(statusLabel), c)
        }

        private fun addLabel(text: String, c: GridBagConstraints) {
            val label = JBLabel(text)
            contentPanel.add(label, c)
        }

        private fun setupListeners() {
            sendButton.addActionListener {
                val projectId = projectIdField.text.trim()
                val token = bearerTokenField.text.trim()
                
                if (projectId.isEmpty() || token.isEmpty()) {
                    statusLabel.text = "Error: Project ID and Bearer Token are required."
                    return@addActionListener
                }

                statusLabel.text = "Sending..."
                sendButton.isEnabled = false

                val manager = PushifyManager(token, projectId)
                
                val data = mutableMapOf<String, String>()
                val deepLink = deepLinkField.text.trim()
                if (deepLink.isNotEmpty()) {
                    data["deeplinkPath"] = deepLink
                }
                data["sound"] = "coin.wav"

                // Run on background thread
                SwingUtilities.invokeLater {
                    Thread {
                        manager.sendNotification(
                            deviceToken = deviceTokenField.text.trim(),
                            title = titleField.text.trim().ifEmpty { null },
                            body = bodyField.text.trim().ifEmpty { null },
                            data = data
                        ) { success, message ->
                            SwingUtilities.invokeLater {
                                statusLabel.text = message
                                sendButton.isEnabled = true
                            }
                        }
                    }.start()
                }
            }
        }

        fun getContent(): JComponent {
            return JBScrollPane(contentPanel)
        }
    }
}
