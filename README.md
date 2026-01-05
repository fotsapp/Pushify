# Pushify - Firebase Push Notification Sender for IntelliJ

![Pushify Logo](pushify-plugin/src/main/resources/pushify.svg)

**Pushify** is an IntelliJ IDEA / Android Studio plugin that allows you to send Firebase Cloud Messaging (FCM) v1 push notifications directly from your IDE. No need to use `curl` commands or external tools anymore!

## Features

*   🚀 **Send Notifications**: Send test notifications to your devices instantly.
*   🛠 **Integrated UI**: Seamlessly integrated into Android Studio / IntelliJ IDEA as a Tool Window.
*   🔒 **Secure**: Uses manual Bearer Token entry. No private keys are stored or transmitted.
*   📝 **Customizable**: Set title, body, and custom data (deeplinks) easily.

## Installation

1.  Open Android Studio / IntelliJ IDEA.
2.  Go to **Settings/Preferences** > **Plugins** > **Marketplace**.
3.  Search for **"Pushify"**.
4.  Click **Install** and restart the IDE.

*(Note: Until published on the marketplace, you can build from source and select "Install Plugin from Disk")*

## How to Use

1.  Open the **Pushify** tool window (usually on the right sidebar).
2.  **Project ID**: Enter your Firebase Project ID (e.g., `my-app-123`).
3.  **Bearer Token**:
    *   You need a valid OAuth 2.0 Access Token to authenticate with FCM.
    *   The easiest way to get one is via `gcloud` CLI:
        ```bash
        gcloud auth print-access-token
        ```
    *   Copy the token and paste it into the "Bearer Token" field.
    *   *For more details, click the "How to get Bearer Token?" link in the plugin.*
4.  **Device Token**: Enter the FCM Registration Token of the target device.
5.  **Message Details**: Fill in the Title, Body, and optional Deeplink.
6.  Click **Send Notification**.

## Development

### Prerequisites
*   JDK 17
*   IntelliJ IDEA Community Edition or Android Studio

### Running Locally
To run the plugin in a sandboxed IDE environment:

```bash
./gradlew :pushify-plugin:runIde
```

### Building
To build the plugin distribution file (`.zip`):

```bash
./gradlew :pushify-plugin:buildPlugin
```
The output file will be located at `pushify-plugin/build/distributions/`.

## License

This project is licensed under the Apache 2.0 License.

